package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.*;
import com.bzw.api.module.basic.model.*;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.basic.param.PushClientParam;
import com.bzw.api.module.basic.param.PushTechnicianParam;
import com.bzw.api.web.WebSocket;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private Gson gson;

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
                if (technician.getBizStatusId().equals(TechnicianState.free.getValue())) {
                    technician.setBizStatusId(TechnicianState.booked.getValue());
                    mapTechnician.put(technician.getId(), technician);
                } else if (technician.getBizStatusId().equals(TechnicianState.serving.getValue())) {
                    technician.setBizStatusId(TechnicianState.servingAndBooked.getValue());
                    mapTechnician.put(technician.getId(), technician);
                }
            }
        }

        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderDetail> orderDetails = Lists.newArrayList();
        List<PushTechnicianParam> pushTechnicianParams = Lists.newArrayList();
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
                PushTechnicianParam pushTechnicianParam = new PushTechnicianParam();
                pushTechnicianParam.setDetailId(orderDetail.getId());
                pushTechnicianParam.setTechnicianName(technician.getName());
                pushTechnicianParam.setRoomId(room.getId());
                pushTechnicianParam.setRoomName(room.getNumber());
                pushTechnicianParam.setProjectId(project.getId());
                pushTechnicianParam.setProjectName(project.getName());
                pushTechnicianParam.setTechnicianId(technician.getId());
                pushTechnicianParam.setTxt("请技师"+technician.getName()+"去"+room.getNumber()+"上钟");
                pushTechnicianParams.add(pushTechnicianParam);
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
        String message = gson.toJson(pushTechnicianParams);
        WebSocket.sendMessage(branchId.toString(),message);
        return order.getId();
    }


    public OrderDetail serve(Long orderDetailId) {
        Date now = new Date();
        OrderDetail orderDetail = orderQueryBiz.getOrderDetail(orderDetailId);
        if (orderDetail == null) {
            return null;
        }
        Order order = orderQueryBiz.getOrder(orderDetail.getOrderId());
        Technician technician = technicianQueryBiz.getTechnicianById(orderDetail.getTechnicianId());
        if (technician == null) {
            return null;
        }
        Preconditions.checkArgument(technician.getBizStatusId().equals(TechnicianState.free.getValue()) ||
                technician.getBizStatusId().equals(TechnicianState.booked.getValue()), "服务未结束不能上钟");
        Room room = roomQueryBiz.getRoom(orderDetail.getRoomId());
        if (room == null) {
            return null;
        }
        Project project = projectQueryBiz.getProject(orderDetail.getProjectId());
        if (project == null) {
            return null;
        }

        List<OrderDetail> orderDetails = orderQueryBiz.listOrderDetail(orderDetail.getOrderId());

        List<Long> technicianIds = Lists.newArrayList();
        for (OrderDetail orderDetail1 : orderDetails) {
            technicianIds.add(orderDetail1.getTechnicianId());
        }
        List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(technicianIds);
        Map<Long, Technician> mapTechnician = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(technicianIds)) {
            technicians.forEach(t -> mapTechnician.put(t.getId(), t));
        }
        int gotoServingTime = 3;
        int max = project.getDuration() + gotoServingTime;
        for (OrderDetail orderDetail1 : orderDetails) {
            Technician technician1 = mapTechnician.get(orderDetail1.getTechnicianId());
            if (technician1 != null && technician1.getOverTime() != null) {
                if (technician1.getBizStatusId().equals(TechnicianState.servingAndBooked.getValue()) &&
                        !technician1.getOrderDetailId().equals(orderDetailId)) {
                    Long minutes = (technician1.getOverTime().getTime() - now.getTime()) / (1000 * 60);
                    int totalMinute = orderDetail1.getDuration() + gotoServingTime + minutes.intValue();
                    if (totalMinute > max) {
                        max = totalMinute;
                    }
                }
            }
        }
        room.setBizStatusId(RoomState.unfinished.getValue());
        if (room.getStartTime() == null) {
            room.setStartTime(now);
        }
        Date overTime = DateUtils.addMinutes(now, project.getDuration());
        room.setOverTime(DateUtils.addMinutes(now, max));
        technician.setBizStatusId(TechnicianState.serving.getValue());
        technician.setStartTime(now);
        technician.setOverTime(overTime);
        technician.setRoomId(room.getId());
        technician.setRoomName(room.getNumber());
        orderDetail.setBeginTime(now);
        orderDetail.setEndTime(overTime);
        orderDetail.setRoomId(room.getId());
        orderDetail.setRoomName(room.getNumber());
        roomEventBiz.update(room);
        technicianEventBiz.updateTechnician(technician);
        orderEventBiz.updateOrderDetail(orderDetail);
        if (StringUtils.isNotBlank(order.getWechatId())) {
            PushClientParam pushClientParam = new PushClientParam();
            pushClientParam.setProjectId(project.getId());
            pushClientParam.setProjectName(project.getName());
            pushClientParam.setRoomId(room.getId());
            pushClientParam.setRoomName(room.getNumber());
            pushClientParam.setTechnicianId(technician.getId());
            pushClientParam.setTechnicianName(technician.getName());
            pushClientParam.setOrderDetailId(orderDetailId);
            String message = gson.toJson(pushClientParam);
            WebSocket.sendMessage(order.getWechatId(), message);
        }
        return orderDetail;
    }
}
