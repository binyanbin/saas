package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.*;
import com.bzw.api.module.basic.model.*;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.platform.model.User;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yanbin
 */
@Service
public class OrderEventService {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private UserQueryBiz userQueryBiz;

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private OrderEventBiz orderEventBiz;

    @Autowired
    private RoomEventBiz roomEventBiz;

    @Autowired
    private TechnicianEventBiz technicianEventBiz;

    public Long addOrderForDesk(List<OrderParam> orderParams, Long userId, Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room == null) {
            return null;
        }
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.free.getValue()), "房间已被预定或正在使用");
        Long orderId = sequenceService.newKey(SeqType.order);
        User user = userQueryBiz.getUser(userId);
        if (user != null) {
            return addOrder(orderId, orderParams, false, userId, room, user.getWechatId());
        } else {
            return null;
        }
    }

    public Long modifyOrderForDesk(Long orderId, List<OrderParam> orderParams, Long userId) {
        Order order = orderQueryBiz.getOrder(orderId);
        User user = userQueryBiz.getUser(userId);
        if (user != null && order != null) {
            orderEventBiz.deleteById(orderId);
            Room room = roomQueryBiz.getRoom(order.getRoomId());
            if (room == null) {
                return null;
            }
            Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.open.getValue()) ||
                    room.getBizStatusId().equals(RoomState.waiting.getValue()), "已服务，无法修改订单");
            return addOrder(orderId, orderParams, true, userId, room, user.getWechatId());
        } else {
            return null;
        }
    }

    public Long addOrderForClient(List<OrderParam> orderParams, String openId, Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room == null) {
            return null;
        }
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.free.getValue()), "房间已被预定或正在使用");
        Long orderId = sequenceService.newKey(SeqType.order);
        return addOrder(orderId, orderParams, false, null, room, openId);
    }

    public Long modifyOrderForClient(Long orderId, List<OrderParam> orderParams, String openId) {
        Order order = orderQueryBiz.getOrder(orderId);
        orderEventBiz.deleteById(orderId);
        Room room = roomQueryBiz.getRoom(order.getRoomId());
        if (room == null) {
            return null;
        }
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.open.getValue()) ||
                room.getBizStatusId().equals(RoomState.waiting.getValue()), "已服务，无法修改订单");

        return addOrder(orderId, orderParams, true, null, room, openId);
    }

    private Long addOrder(Long orderId, List<OrderParam> orderParams, boolean isUpdate, Long userId, Room room, String openId) {
        Date now = new Date();
        Long tenantId = room.getTenantId();
        Long branchId = room.getBranchId();
        Order order = new Order();
        if (!isUpdate) {
            order.setId(orderId);
            order.setTenantId(tenantId);
            order.setBranchId(branchId);
            order.setCreatedTime(now);
            order.setBizStatusId(OrderState.non_payment.getValue());
            order.setWechatId(openId);
            order.setRoomId(room.getId());
            order.setUserId(userId);
        } else {
            order = orderQueryBiz.getOrder(orderId);
        }
        List<Long> detailIds = sequenceService.newKeys(SeqType.orderDetail, orderParams.size());
        List<Integer> projectIds = Lists.newArrayList();
        List<Long> technicianIds = Lists.newArrayList();
        for (OrderParam orderParam : orderParams) {
            projectIds.add(orderParam.getProjectId());
            technicianIds.add(orderParam.getTechnicianId());
        }
        List<Project> projects = projectQueryBiz.listProjectByIds(projectIds);
        Map<Integer, Project> mapProject = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(projects)) {
            projects.forEach(t -> mapProject.put(t.getId(), t));
        }

        List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(technicianIds);
        Map<Long, Technician> mapTechnician = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(technicians)) {
            for (Technician technician : technicians) {
                mapTechnician.put(technician.getId(), technician);
                if (technician.getBizStatusId().equals(TechnicianState.free.getValue())) {
                    technician.setBizStatusId(TechnicianState.booked.getValue());
                } else if (technician.getBizStatusId().equals(TechnicianState.booked.getValue()) ||
                        technician.getBizStatusId().equals(TechnicianState.serving.getValue())) {
                    technician.setBizStatusId(TechnicianState.servingAndBooked.getValue());
                }
            }
        }

        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderDetail> orderDetails = Lists.newArrayList();
        for (int i = 0; i < orderParams.size(); i++) {
            if (mapProject.containsKey(orderParams.get(i).getProjectId()) &&
                    mapTechnician.containsKey(orderParams.get(i).getTechnicianId())) {
                Project project = mapProject.get(orderParams.get(i).getProjectId());
                Technician technician = mapTechnician.get(orderParams.get(i).getTechnicianId());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setBizStatusId(OrderDetailState.booked.getValue());
                orderDetail.setTenantId(room.getTenantId());
                orderDetail.setBranchId(branchId);
                orderDetail.setId(detailIds.get(i));
                orderDetail.setBookTime(now);
                orderDetail.setOrderId(order.getId());
                orderDetail.setRoomId(room.getId());
                orderDetail.setProjectId(orderParams.get(i).getProjectId());
                orderDetail.setTechnicianId(orderParams.get(i).getTechnicianId());
                orderDetail.setBranchName(room.getBranchName());
                orderDetail.setPrice(project.getPrice());
                orderDetail.setProjectName(project.getName());
                orderDetail.setTechnicianName(technician.getName());
                orderDetail.setRoomName(room.getNumber());
                orderDetail.setTypeId(OrderType.CustomerReservation.getValue());
                orderDetail.setDuration(project.getDuration());
                orderDetails.add(orderDetail);
                totalPrice = totalPrice.add(project.getPrice());
            }
        }
        order.setPrice(totalPrice);
        if (!isUpdate) {
            orderEventBiz.add(order);
        } else {
            order = new Order();
            order.setId(orderId);
            order.setPrice(totalPrice);
            order.setModifiedTime(now);
            order.setRoomId(room.getId());
            order.setWechatId(openId);
            orderEventBiz.update(order);
        }
        orderEventBiz.addOrderDetails(orderDetails);
        room.setOrderId(orderId);
        room.setBizStatusId(RoomState.waiting.getValue());
        roomEventBiz.update(room);
        technicianEventBiz.updateList(technicians);
        return order.getId();
    }


    public boolean serve(Long orderDetailId) {
        Date now = new Date();
        OrderDetail orderDetail = orderQueryBiz.getOrderDetail(orderDetailId);
        if (orderDetail == null) {
            return false;
        }
        Technician technician = technicianQueryBiz.getTechnicianById(orderDetail.getTechnicianId());
        if (technician == null) {
            return false;
        }
        Preconditions.checkArgument(technician.getBizStatusId().equals(TechnicianState.free.getValue()) ||
                technician.getBizStatusId().equals(TechnicianState.booked.getValue()), "服务未结束不能上钟");
        Room room = roomQueryBiz.getRoom(orderDetail.getRoomId());
        if (room == null) {
            return false;
        }
        Project project = projectQueryBiz.getProject(orderDetail.getProjectId());
        if (project == null) {
            return false;
        }
        room.setBizStatusId(RoomState.unfinished.getValue());
        technician.setBizStatusId(TechnicianState.serving.getValue());
        orderDetail.setBeginTime(now);
        orderDetail.setEndTime(DateUtils.addMinutes(now, project.getDuration()));
        roomEventBiz.update(room);
        technicianEventBiz.updateTechnician(technician);
        orderEventBiz.updateOrderDetail(orderDetail);
        return true;
    }
}
