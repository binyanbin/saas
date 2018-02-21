package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.*;
import com.bzw.api.module.main.biz.*;
import com.bzw.api.module.main.constant.LogConstants;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.enums.*;
import com.bzw.api.module.main.params.OrderParam;
import com.bzw.api.module.main.params.PushClientParam;
import com.bzw.api.module.main.params.PushTechnicianParam;
import com.bzw.api.module.mq.params.MessageParam;
import com.bzw.api.module.mq.service.MqProducer;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.bzw.common.utils.DtUtils;
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
    private RecordChangeEventBiz recordChangeEventBiz;

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    private Gson gson;

    /**
     * 前台预定服务
     */
    public Long addOrderForDesk(List<OrderParam> orderParams, Long userId, Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkNotNull(room, WarnMessage.NOT_FOUND_ROOM);
        Preconditions.checkState(room.getBizStatusId().equals(RoomState.free.getValue()), WarnMessage.ROOM_USED);
        Long orderId = sequenceService.newKey(SeqType.order);
        User user = userQueryBiz.getUser(userId);
        if (user != null) {
            return addOrder(orderId, orderParams, false, userId, room, user.getWechatId());
        } else {
            return null;
        }
    }

    /**
     * 前台修改订单
     */
    public Long modifyOrderForDesk(Long orderId, List<OrderParam> orderParams, Long userId) {
        Order order = orderQueryBiz.getOrder(orderId);
        User user = userQueryBiz.getUser(userId);
        if (user != null && order != null) {
            orderEventBiz.deleteById(orderId);
            Room room = roomQueryBiz.getRoom(order.getRoomId());
            if (room == null) {
                return null;
            }
            Preconditions.checkState(room.getBizStatusId().equals(RoomState.open.getValue()) ||
                    room.getBizStatusId().equals(RoomState.waiting.getValue()), "已服务，无法修改订单");
            return addOrder(orderId, orderParams, true, userId, room, user.getWechatId());
        } else {
            return null;
        }
    }

    /**
     * 客人预定服务
     */
    public Long addOrderForClient(List<OrderParam> orderParams, String openId, Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room == null) {
            return null;
        }
        String message;
        if (room.getBizStatusId().equals(RoomState.free.getValue())) {
            message = WarnMessage.ROOM_NOT_OPEN;
        } else {
            message = WarnMessage.ROOM_USED;
        }
        Preconditions.checkState(room.getBizStatusId().equals(RoomState.open.getValue()), message);
        Long orderId = sequenceService.newKey(SeqType.order);
        return addOrder(orderId, orderParams, false, null, room, openId);
    }

    /**
     * 客人修改订单
     */
    public Long modifyOrderForClient(Long orderId, List<OrderParam> orderParams, String openId) {
        Order order = orderQueryBiz.getOrder(orderId);
        orderEventBiz.deleteById(orderId);
        Room room = roomQueryBiz.getRoom(order.getRoomId());
        if (room == null) {
            return null;
        }
        Preconditions.checkState(room.getBizStatusId().equals(RoomState.open.getValue()) ||
                room.getBizStatusId().equals(RoomState.waiting.getValue()), WarnMessage.SERVICE_BEGIN);

        return addOrder(orderId, orderParams, true, null, room, openId);
    }

    /**
     * 技师上钟
     */
    public OrderDetail serve(Long orderDetailId) {
        Date now = new Date();
        OrderDetail orderDetail = orderQueryBiz.getOrderDetail(orderDetailId);
        Preconditions.checkNotNull(orderDetail, WarnMessage.NOT_FOUND_ORDER);
        Preconditions.checkState(orderDetail.getBizStatusId().equals(OrderDetailState.booked.getValue()), WarnMessage.TECHNICIAN_SERVING);
        Order order = orderQueryBiz.getOrder(orderDetail.getOrderId());
        Technician technician = technicianQueryBiz.getTechnicianById(orderDetail.getTechnicianId());
        if (technician == null) {
            return null;
        }
        Preconditions.checkArgument(technician.getBizStatusId().equals(TechnicianState.free.getValue()) ||
                technician.getBizStatusId().equals(TechnicianState.booked.getValue()), WarnMessage.TECHNICIAN_CANNOT_SERVING);
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
        technician.setRoomName(room.getName());
        orderDetail.setBeginTime(now);
        orderDetail.setEndTime(overTime);
        orderDetail.setRoomId(room.getId());
        orderDetail.setRoomName(room.getName());
        orderDetail.setBizStatusId(OrderDetailState.Serving.getValue());
        roomEventBiz.update(room);
        technicianEventBiz.updateTechnician(technician);
        orderEventBiz.updateOrderDetail(orderDetail);

        PushClientParam pushClientParam = new PushClientParam();
        pushClientParam.setProjectId(project.getId());
        pushClientParam.setProjectName(project.getName());
        pushClientParam.setRoomId(room.getId());
        pushClientParam.setRoomName(room.getName());
        pushClientParam.setTechnicianId(technician.getId());
        pushClientParam.setTechnicianName(technician.getName());
        pushClientParam.setOrderDetailId(orderDetailId);
        pushClientParam.setText(technician.getName() + WarnMessage.TECHNICIAN_HURRY);
        String message = gson.toJson(pushClientParam);
        MessageParam messageParam = new MessageParam();
        messageParam.setJson(message);
        messageParam.setMessage(pushClientParam.getText());
        if (order.getWechatId() != null) {
            messageParam.setReceiver(order.getWechatId());
        } else if (order.getUserId() != null) {
            messageParam.setReceiver(order.getUserId().toString());
        }
        if (StringUtils.isNotBlank(messageParam.getReceiver())) {
            mqProducer.send(messageParam);
        }

        return orderDetail;
    }

    /**
     * 订单支付
     */
    public Order pay(Long orderId, BigDecimal price) {
        Order order = orderQueryBiz.getOrder(orderId);
        Preconditions.checkNotNull(order, WarnMessage.NOT_FOUND_ORDER);
        Preconditions.checkState(order.getBizStatusId().equals(OrderState.non_payment.getValue()), WarnMessage.ORDER_PAID);
        if (price == null) {
            price = order.getPrice();
        }
        Date now = new Date();
        order.setPayPrice(price);
        order.setPayTime(now);
        order.setBizStatusId(OrderState.paid.getValue());
        order.setModifiedTime(now);
        orderEventBiz.update(order);
        recordChangeEventBiz.add(RecordChangeType.order, order.getId(), order.getTenantId(), now,
                String.format(LogConstants.ORDER_PAY_LOG, DtUtils.toDateString(now), price.toString()));
        return order;
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

        Map<Integer, Project> mapProject = getIntegerProjectMap(projectIds);
        List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(technicianIds);
        Map<Long,Technician> mapTechnician = getTechnicianMap(technicians);

        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderDetail> orderDetails = Lists.newArrayList();
        List<PushTechnicianParam> pushTechnicianParams = Lists.newArrayList();
        for (int i = 0; i < orderParams.size(); i++) {
            if (mapProject.containsKey(orderParams.get(i).getProjectId()) &&
                    mapTechnician.containsKey(orderParams.get(i).getTechnicianId())) {
                Project project = mapProject.get(orderParams.get(i).getProjectId());
                Technician technician = mapTechnician.get(orderParams.get(i).getTechnicianId());
                OrderDetail orderDetail = getOrderDetail(orderParams.get(i), room, now, branchId, order, detailIds.get(i), project, technician);
                orderDetails.add(orderDetail);
                totalPrice = totalPrice.add(project.getPrice());
                PushTechnicianParam pushTechnicianParam = new PushTechnicianParam();
                pushTechnicianParam.setDetailId(orderDetail.getId());
                pushTechnicianParam.setTechnicianName(technician.getName());
                pushTechnicianParam.setRoomId(room.getId());
                pushTechnicianParam.setRoomName(room.getName());
                pushTechnicianParam.setProjectId(project.getId());
                pushTechnicianParam.setProjectName(project.getName());
                pushTechnicianParam.setTechnicianId(technician.getId());
                pushTechnicianParam.setTxt("请技师" + technician.getName() + "去" + room.getName() + "上钟");
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

        recordChangeEventBiz.add(RecordChangeType.room, room.getId(), room.getTenantId(), now,
                String.format(LogConstants.ROOM_BOOK_LOG, DtUtils.toDateString(now)));

        technicianEventBiz.updateList(technicians);

        pushMessageToTechnician(branchId, pushTechnicianParams);

        if (!isUpdate) {
            recordChangeEventBiz.add(RecordChangeType.order, order.getId(), order.getTenantId(), now,
                    String.format(LogConstants.ORDER_CREATE_LOG, DtUtils.toDateString(now)));
        } else {
            recordChangeEventBiz.add(RecordChangeType.order, order.getId(), order.getTenantId(), now,
                    String.format(LogConstants.ORDER_MODIFY_LOG, DtUtils.toDateString(now)));
        }
        return order.getId();
    }

    private OrderDetail getOrderDetail(OrderParam orderParam, Room room, Date now, Long branchId, Order order, Long detailId,  Project project, Technician technician) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setBizStatusId(OrderDetailState.booked.getValue());
        orderDetail.setTenantId(room.getTenantId());
        orderDetail.setBranchId(branchId);
        orderDetail.setId(detailId);
        orderDetail.setBookTime(now);
        orderDetail.setOrderId(order.getId());
        orderDetail.setRoomId(room.getId());
        orderDetail.setProjectId(orderParam.getProjectId());
        orderDetail.setTechnicianId(orderParam.getTechnicianId());
        orderDetail.setBranchName(room.getBranchName());
        orderDetail.setPrice(project.getPrice());
        orderDetail.setProjectName(project.getName());
        orderDetail.setTechnicianName(technician.getName());
        orderDetail.setRoomName(room.getName());
        orderDetail.setTypeId(OrderType.CustomerReservation.getValue());
        orderDetail.setDuration(project.getDuration());
        return orderDetail;
    }

    private void pushMessageToTechnician(Long branchId, List<PushTechnicianParam> pushTechnicianParams) {
        for (PushTechnicianParam pushTechnicianParam : pushTechnicianParams) {
            String message = gson.toJson(pushTechnicianParam);
            MessageParam messageParam = new MessageParam();
            messageParam.setJson(message);
            messageParam.setReceiver(branchId.toString());
            messageParam.setMessage(pushTechnicianParam.getTxt());
            mqProducer.send(messageParam);
        }
    }

    private Map<Long, Technician> getTechnicianMap(List<Technician> technicians) {
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
        return mapTechnician;
    }

    private Map<Integer, Project> getIntegerProjectMap(List<Integer> projectIds) {
        List<Project> projects = projectQueryBiz.listProjectByIds(projectIds);
        Map<Integer, Project> mapProject = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(projects)) {
            projects.forEach(t -> mapProject.put(t.getId(), t));
        }
        return mapProject;
    }

}
