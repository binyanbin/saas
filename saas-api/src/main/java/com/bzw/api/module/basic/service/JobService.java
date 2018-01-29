package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.OrderEventBiz;
import com.bzw.api.module.basic.biz.OrderQueryBiz;
import com.bzw.api.module.basic.biz.RoomEventBiz;
import com.bzw.api.module.basic.biz.RoomQueryBiz;
import com.bzw.api.module.basic.enums.OrderDetailState;
import com.bzw.api.module.basic.enums.RoomState;
import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.Room;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Component
public class JobService {

    private final static long ONE_Minute = 60 * 1000;

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private TechnicianEventService technicianEventService;

    @Autowired
    private OrderEventBiz orderEventBiz;

    @Autowired
    private RoomEventBiz roomEventBiz;

    @Scheduled(fixedDelay = ONE_Minute, initialDelay = ONE_Minute)
    public void finishOrder() {
        Date now = new Date();
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " >>rate task executed....");
        List<Room> roomList = roomQueryBiz.listRoomByRoomSate(RoomState.unfinished);
        for (Room room : roomList) {
            if (room.getOverTime().compareTo(now) >= 0) {
                List<OrderDetail> orderDetailList = orderQueryBiz.listOrderDetail(room.getOrderId());
                boolean isFinish =true;
                for (OrderDetail orderDetail : orderDetailList) {
                    if (orderDetail.getEndTime().compareTo(now) >= 0){
                        orderDetail.setBizStatusId(OrderDetailState.finished.getValue());
                        technicianEventService.finishTechnician(orderDetail.getTechnicianId());
                        orderEventBiz.updateOrderDetail(orderDetail);
                    }
                    else {
                        isFinish =false;
                    }
                }
                if (isFinish){
                    room.setOrderId(null);
                    room.setStartTime(null);
                    room.setOverTime(null);
                    room.setBizStatusId(RoomState.free.getValue());
                    room.setModifiedTime(now);
                    roomEventBiz.update(room);
                }
            }
        }

    }
}

