package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.*;
import com.bzw.api.module.basic.model.*;
import com.bzw.common.sequence.ISequence;
import com.bzw.common.sequence.SeqType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author yanbin
 */
@Service
public class RoomEventService {

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private EmployeeQueryBiz employeeQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private RoomEventBiz roomEventBiz;

    @Autowired
    private RecordChangeEventBiz recordChangeEventBiz;

    @Autowired
    private ISequence sequenceService;

    @Autowired
    private TechnicianEventBiz technicianEventBiz;

    @Autowired
    private OrderEventBiz orderEventBiz;

    public boolean open(Long roomId, Long employeeId) {
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkArgument(room != null, "房间id不存在");
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.free.getValue()) ||
                room.getBizStatusId().equals(RoomState.booked.getValue()), "房间已被使用");
        return updateRoomState(room, employeeId, RoomState.open.getValue());
    }

    public boolean book(Long roomId, Long employeeId) {
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkArgument(room != null, "房间id不存在");
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.free.getValue()), "房间已被预定或使用");
        return updateRoomState(room, employeeId, RoomState.booked.getValue());
    }

    public boolean cancel(Long roomId, Long employeeId) {
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkArgument(room != null, "房间id不存在");
        Date now = new Date();
        if (room.getOrderId() > 0L) {
            Order order = orderQueryBiz.getOrder(room.getOrderId());
            List<OrderDetail> orderDetailList = orderQueryBiz.listOrderDetail(order.getId());
            Set<Long> technicianIds = Sets.newHashSet();
            for (OrderDetail orderDetail : orderDetailList) {
                technicianIds.add(orderDetail.getTechnicianId());
            }
            List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(Lists.newArrayList(technicianIds));
            order.setBizStatusId(OrderState.cancel.getValue());
            order.setModifiedTime(now);
            for (OrderDetail orderDetail : orderDetailList) {
                orderDetail.setBizStatusId(OrderDetailState.cancel.getValue());
                orderDetail.setBeginTime(null);
                orderDetail.setEndTime(null);
            }
            for (Technician technician : technicians) {
                technician.setBizStatusId(TechnicianState.free.getValue());
                technician.setStartTime(null);
                technician.setOverTime(null);
            }
            orderEventBiz.update(order);
            orderEventBiz.updateOrderDetails(orderDetailList);
            technicianEventBiz.updateList(technicians);
        }
        room.setOrderId(0L);
        room.setStartTime(null);
        room.setOverTime(null);
        room.setBizStatusId(RoomState.free.getValue());
        room.setModifiedId(employeeId);
        room.setModifiedTime(now);
        roomEventBiz.update(room);
        return true;
    }

    private boolean updateRoomState(Room room, Long employeeId, Integer statusId) {
        if (room.getBizStatusId().equals(statusId)) {
            return false;
        }
        Date now = new Date();
        Employee employee = employeeQueryBiz.getEmployee(employeeId);
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdEqualTo(room.getId()).andVersionIdEqualTo(room.getVersionId());
        Room updateRoom = new Room();
        updateRoom.setBizStatusId(statusId);
        updateRoom.setModifiedTime(now);
        updateRoom.setModifiedId(employee.getId());
        updateRoom.setVersionId(room.getVersionId() + 1);
        updateRoom.setStartTime(now);
        updateRoom.setOverTime(null);
        int result = roomEventBiz.update(updateRoom, room.getId(), room.getVersionId());
        if (result > 0) {
            RecordChange recordChange = new RecordChange();
            recordChange.setId(sequenceService.newKey(SeqType.recordChange));
            recordChange.setChnageDate(now);
            recordChange.setContent(employee.getName() + "把房态[" + RoomState.parse(room.getBizStatusId()).getDesc() + "]修改为:[" + RoomState.parse(statusId).getDesc() + "]");
            recordChange.setDocumentId(room.getId());
            recordChange.setTenantId(room.getTenantId());
            recordChange.setType(RecordChangeType.room.getValue());
            recordChangeEventBiz.add(recordChange);
            return true;
        } else {
            return false;
        }
    }

//    public boolean updateRoomState(Long roomId, Integer statusId, Long employeeId) {
//        Room room = roomQueryBiz.getRoom(roomId);
//        if (room.getBizStatusId().equals(statusId)) {
//            return false;
//        }
//        Date now = new Date();
//        Employee employee = employeeQueryBiz.getEmployee(employeeId);
//        RoomExample roomExample = new RoomExample();
//        roomExample.createCriteria().andIdEqualTo(roomId).andVersionIdEqualTo(room.getVersionId());
//        Room updateRoom = new Room();
//        updateRoom.setBizStatusId(statusId);
//        updateRoom.setModifiedTime(now);
//        updateRoom.setModifiedId(employee.getId());
//        updateRoom.setVersionId(room.getVersionId() + 1);
//        List<Integer> statusIds = Lists.newArrayList();
//        statusIds.add(RoomState.free.getValue());
//        statusIds.add(RoomState.cleaning.getValue());
//        statusIds.add(RoomState.pause.getValue());
//        List<OrderDetail> orderDetails = orderQueryBiz.listOrderDetail(room.getOrderId());
//        Set<Long> technicianIds = Sets.newHashSet();
//        Map<Long, OrderDetail> mapOrderDetail = Maps.newHashMap();
//        if (statusIds.contains(statusId)) {
//            updateRoom.setOrderId(0L);
//            updateRoom.setStartTime(null);
//            updateRoom.setOverTime(null);
//            for (OrderDetail orderDetail : orderDetails) {
//                orderDetail.setBizStatusId(OrderDetailState.finished.getValue());
//                mapOrderDetail.put(orderDetail.getTechnicianId(), orderDetail);
//                technicianIds.add(orderDetail.getTechnicianId());
//            }
//            orderEventBiz.updateOrderDetails(orderDetails);
//            if (!CollectionUtils.isEmpty(technicianIds)) {
//                List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(Lists.newArrayList(technicianIds));
//                for (Technician technician : technicians) {
//                    OrderDetail orderDetail = mapOrderDetail.get(technician.getId());
//                    technician.setBizStatusId(TechnicianState.free.getValue());
//                    if (orderDetail != null) {
//                        technician.setStartTime(null);
//                        technician.setOverTime(null);
//                    }
//                }
//                technicianEventBiz.updateTechnicians(technicians);
//            }
//        } else if (statusId.equals(RoomState.serving.getValue())) {
//            int max = 0;
//            for (OrderDetail orderDetail : orderDetails) {
//                mapOrderDetail.put(orderDetail.getTechnicianId(), orderDetail);
//                technicianIds.add(orderDetail.getTechnicianId());
//                if (orderDetail.getDuration() == null) {
//                    orderDetail.setDuration(0);
//                }
//                if (orderDetail.getDuration() > max) {
//                    max = orderDetail.getDuration();
//                    orderDetail.setBeginTime(now);
//                    orderDetail.setEndTime(DateUtils.addMinutes(now, orderDetail.getDuration()));
//                    orderDetail.setBizStatusId(OrderDetailState.Serving.getValue());
//                }
//            }
//            room.setStartTime(now);
//            room.setOverTime(DateUtils.addMinutes(now, max));
//            roomEventBiz.updateRoom(room);
//            orderEventBiz.updateOrderDetails(orderDetails);
//            if (!CollectionUtils.isEmpty(technicianIds)) {
//                List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(Lists.newArrayList(technicianIds));
//                for (Technician technician : technicians) {
//                    OrderDetail orderDetail = mapOrderDetail.get(technician.getId());
//                    technician.setBizStatusId(TechnicianState.serving.getValue());
//                    if (orderDetail != null) {
//                        technician.setStartTime(orderDetail.getBeginTime());
//                        technician.setOverTime(orderDetail.getEndTime());
//                    }
//                }
//                technicianEventBiz.updateTechnicians(technicians);
//            }
//        }
//        if (statusId.equals(RoomState.using.getValue())) {
//            room.setStartTime(now);
//            roomEventBiz.updateRoom(room);
//        }
//        int result = roomEventBiz.updateRoom(updateRoom, roomId, room.getVersionId());
//        if (result > 0) {
//            RecordChange recordChange = new RecordChange();
//            recordChange.setId(sequenceService.newKey(SeqType.recordChange));
//            recordChange.setChnageDate(now);
//            recordChange.setContent(employee.getName() + "把房态[" + RoomState.parse(room.getBizStatusId()).getDesc() + "]修改为:[" + RoomState.parse(statusId).getDesc() + "]");
//            recordChange.setDocumentId(room.getId());
//            recordChange.setTenantId(room.getTenantId());
//            recordChange.setType(RecordChangeType.room.getValue());
//            recordChangeEventBiz.add(recordChange);
//            return true;
//        } else {
//            return false;
//        }
//    }
}
