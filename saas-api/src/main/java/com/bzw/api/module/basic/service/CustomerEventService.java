package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.OrderDetailState;
import com.bzw.api.module.basic.enums.OrderState;
import com.bzw.api.module.basic.enums.OrderType;
import com.bzw.api.module.basic.model.*;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.common.content.WebSession;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerEventService {

    @Autowired
    private RoomEventBiz roomEventBiz;

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private UserEventBiz userEventBiz;

    @Autowired
    private OrderEventBiz orderEventBiz;

    @Autowired
    private SequenceService sequenceService;


    public boolean updateRoomState(Long roomId, Integer statusId, Long employeeId) {
        return roomEventBiz.updateRoomState(roomId, statusId, employeeId);
    }

    public WebSession login(String code, String password) {
        return userEventBiz.Login(code, password);
    }

    public WebSession openIdLogin(String openId) {
        return userEventBiz.openIdLogin(openId);
    }

    public boolean bindOpenId(Long employeeId, String openId) {
        return userEventBiz.bindOpenId(employeeId, openId);
    }

    public Long addOrder(List<OrderParam> orderParams) {
        Long orderId = sequenceService.newKey(SeqType.order);
        return addOrder(orderId, orderParams, false);
    }

    private Long addOrder(Long orderId, List<OrderParam> orderParams, boolean isUpdate) {
        Date now = new Date();
        Room room = roomQueryBiz.getRoom(orderParams.get(0).getRoomId());
        if (room == null)
            return null;
        Long tenantId = room.getTenantId();
        Long branchId = room.getBranchId();
        Order order = new Order();
        if (!isUpdate) {
            order.setId(orderId);
            order.setTenantId(tenantId);
            order.setBranchId(branchId);
            order.setCreatedTime(now);
            order.setBizStatusId(OrderState.non_payment.getValue());
        } else {
            order = orderQueryBiz.getOrder(orderId);
        }
        List<Long> detailIds = sequenceService.newKeys(SeqType.orderDetail, orderParams.size());
        List<Integer> projectIds = Lists.newArrayList();
        List<Long> technicianIds = Lists.newArrayList();
        List<Long> roomIds = Lists.newArrayList();
        for (OrderParam orderParam : orderParams) {
            projectIds.add(orderParam.getProjectId());
            technicianIds.add(orderParam.getTechnicianId());
            roomIds.add(orderParam.getRoomId());
        }
        List<Project> projects = projectQueryBiz.listProjectByIds(projectIds);
        Map<Integer, Project> mapProject = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(projects)) {
            projects.forEach(t -> mapProject.put(t.getId(), t));
        }

        List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(technicianIds);
        Map<Long, Technician> mapTechnician = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(technicians)) {
            technicians.forEach(t -> mapTechnician.put(t.getId(), t));
        }

        List<Room> rooms = roomQueryBiz.listRoomByIds(roomIds);
        Map<Long, Room> mapRoom = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(rooms)) {
            rooms.forEach(t -> mapRoom.put(t.getId(), t));
        }

        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderDetail> orderDetails = Lists.newArrayList();
        for (int i = 0; i < orderParams.size(); i++) {
            if (mapProject.containsKey(orderParams.get(i).getProjectId()) &&
                    mapTechnician.containsKey(orderParams.get(i).getTechnicianId()) &&
                    mapRoom.containsKey(orderParams.get(i).getRoomId())) {
                Project project = mapProject.get(orderParams.get(i).getProjectId());
                Technician technician = mapTechnician.get(orderParams.get(i).getTechnicianId());
                Room room1 = mapRoom.get(orderParams.get(i).getRoomId());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setBizStatusId(OrderDetailState.booked.getValue());
                orderDetail.setTenantId(room1.getTenantId());
                orderDetail.setBranchId(branchId);
                orderDetail.setId(detailIds.get(i));
                orderDetail.setBookTime(now);
                orderDetail.setOrderId(order.getId());
                orderDetail.setRoomId(orderParams.get(i).getRoomId());
                orderDetail.setProjectId(orderParams.get(i).getProjectId());
                orderDetail.setTechnicianId(orderParams.get(i).getTechnicianId());
                orderDetail.setBranchName(room.getBranchName());
                orderDetail.setPrice(project.getPrice());
                orderDetail.setProjectName(project.getName());
                orderDetail.setTechnicianName(technician.getName());
                orderDetail.setRoomName(room1.getNumber());
                orderDetail.setTypeId(OrderType.CustomerReservation.getValue());
                orderDetails.add(orderDetail);
                totalPrice = totalPrice.add(project.getPrice());
            }
        }
        order.setPrice(totalPrice);
        if (!isUpdate)
            orderEventBiz.add(order);
        else {
            order = new Order();
            order.setId(orderId);
            order.setPrice(totalPrice);
            order.setModifiedTime(now);
            orderEventBiz.update(order);
        }
        orderEventBiz.addOrderDetails(orderDetails);
        room.setOrderId(orderId);
        roomEventBiz.updateRoom(room);
        return order.getId();
    }

    public Long modifyOrder(Long orderId, List<OrderParam> orderParams) {
        orderEventBiz.deleteById(orderId);
        return addOrder(orderId, orderParams, true);
    }
}
