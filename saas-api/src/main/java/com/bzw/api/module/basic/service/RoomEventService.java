package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.constant.LogConstants;
import com.bzw.api.module.basic.constant.WarnMessage;
import com.bzw.api.module.basic.enums.*;
import com.bzw.api.module.basic.model.*;
import com.bzw.common.sequence.ISequence;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.utils.DtUtils;
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

    @Autowired
    private TechnicianEventService technicianEventService;

    /**
     * 开房
     */
    public boolean open(Long roomId, Long employeeId) {
        Date now = new Date();
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkArgument(room != null, WarnMessage.NOT_FOUND_ROOM);
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.free.getValue()) ||
                room.getBizStatusId().equals(RoomState.booked.getValue()), WarnMessage.ROOM_USED);
        recordChangeEventBiz.add(RecordChangeType.room, room.getId(), room.getTenantId(), now,
                String.format(LogConstants.ROOM_OPEN_LOG, DtUtils.toString(now)));
        return updateRoomState(room, employeeId, RoomState.open.getValue());
    }

    /**
     * 订房
     */
    public boolean book(Long roomId, Long employeeId) {
        Date now = new Date();
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkArgument(room != null, WarnMessage.NOT_FOUND_ROOM);
        Preconditions.checkArgument(room.getBizStatusId().equals(RoomState.free.getValue()), "房间已被预定或使用");
        recordChangeEventBiz.add(RecordChangeType.room, room.getId(), room.getTenantId(), now,
                String.format(LogConstants.ROOM_BOOK_LOG, DtUtils.toString(now)));
        return updateRoomState(room, employeeId, RoomState.booked.getValue());
    }

    /**
     * 取消房间
     */
    public boolean cancel(Long roomId, Long employeeId) {
        Room room = roomQueryBiz.getRoom(roomId);
        Preconditions.checkArgument(room != null, WarnMessage.NOT_FOUND_ROOM);
        Date now = new Date();
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
            technicianEventService.freeTechnician(technician.getId());
        }
        orderEventBiz.update(order);
        orderEventBiz.updateOrderDetails(orderDetailList);
        technicianEventBiz.updateList(technicians);
        room.setOrderId(null);
        room.setStartTime(null);
        room.setOverTime(null);
        room.setBizStatusId(RoomState.free.getValue());
        room.setModifiedId(employeeId);
        room.setModifiedTime(now);
        roomEventBiz.update(room);
        recordChangeEventBiz.add(RecordChangeType.room, room.getId(), room.getTenantId(), now,
                String.format(LogConstants.ROOM_CANCEL_LOG, DtUtils.toString(now)));
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
}
