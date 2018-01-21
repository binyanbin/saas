package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.OrderDetailState;
import com.bzw.api.module.basic.enums.RecordChangeType;
import com.bzw.api.module.basic.enums.RoomState;
import com.bzw.api.module.basic.enums.TechnicianState;
import com.bzw.api.module.basic.model.*;
import com.bzw.common.sequence.ISequence;
import com.bzw.common.sequence.SeqType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public boolean updateRoomState(Long roomId, Integer statusId, Long employeeId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room.getBizStatusId().equals(statusId)) {
            return false;
        }
        Date now = new Date();
        Employee employee = employeeQueryBiz.getEmployee(employeeId);
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdEqualTo(roomId).andVersionIdEqualTo(room.getVersionId());
        Room updateRoom = new Room();
        updateRoom.setBizStatusId(statusId);
        updateRoom.setModifiedTime(now);
        updateRoom.setModifiedId(employee.getId());
        updateRoom.setVersionId(room.getVersionId() + 1);
        List<Integer> statusIds = Lists.newArrayList();
        statusIds.add(RoomState.free.getValue());
        statusIds.add(RoomState.cleaning.getValue());
        statusIds.add(RoomState.pause.getValue());
        List<OrderDetail> orderDetails = orderQueryBiz.listOrderDetail(room.getOrderId());
        Set<Long> technicianIds = Sets.newHashSet();
        Map<Long, OrderDetail> mapOrderDetail = Maps.newHashMap();
        if (statusIds.contains(statusId)) {
            updateRoom.setOrderId(0L);
            updateRoom.setStartTime(null);
            updateRoom.setOverTime(null);
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setBizStatusId(OrderDetailState.finished.getValue());
                mapOrderDetail.put(orderDetail.getTechnicianId(), orderDetail);
                technicianIds.add(orderDetail.getTechnicianId());
            }
            orderEventBiz.updateOrderDetails(orderDetails);
            if (!CollectionUtils.isEmpty(technicianIds)) {
                List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(Lists.newArrayList(technicianIds));
                for (Technician technician : technicians) {
                    OrderDetail orderDetail = mapOrderDetail.get(technician.getId());
                    technician.setBizStatusId(TechnicianState.free.getValue());
                    if (orderDetail != null) {
                        technician.setStartTime(null);
                        technician.setOverTime(null);
                    }
                }
                technicianEventBiz.updateTechnicians(technicians);
            }
        } else if (statusId.equals(RoomState.serving.getValue())) {
            int max = 0;
            for (OrderDetail orderDetail : orderDetails) {
                mapOrderDetail.put(orderDetail.getTechnicianId(), orderDetail);
                technicianIds.add(orderDetail.getTechnicianId());
                if (orderDetail.getDuration() == null) {
                    orderDetail.setDuration(0);
                }
                if (orderDetail.getDuration() > max) {
                    max = orderDetail.getDuration();
                    orderDetail.setBeginTime(now);
                    orderDetail.setEndTime(DateUtils.addMinutes(now, orderDetail.getDuration()));
                    orderDetail.setBizStatusId(OrderDetailState.Serving.getValue());
                }
            }
            room.setStartTime(now);
            room.setOverTime(DateUtils.addMinutes(now, max));
            roomEventBiz.updateRoom(room);
            orderEventBiz.updateOrderDetails(orderDetails);
            if (!CollectionUtils.isEmpty(technicianIds)) {
                List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(Lists.newArrayList(technicianIds));
                for (Technician technician : technicians) {
                    OrderDetail orderDetail = mapOrderDetail.get(technician.getId());
                    technician.setBizStatusId(TechnicianState.serving.getValue());
                    if (orderDetail != null) {
                        technician.setStartTime(orderDetail.getBeginTime());
                        technician.setOverTime(orderDetail.getEndTime());
                    }
                }
                technicianEventBiz.updateTechnicians(technicians);
            }
        }
        if (statusId.equals(RoomState.using.getValue())) {
            room.setStartTime(now);
            roomEventBiz.updateRoom(room);
        }
        int result = roomEventBiz.updateRoom(updateRoom, roomId, room.getVersionId());
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
