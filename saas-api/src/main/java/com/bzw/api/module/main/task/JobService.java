package com.bzw.api.module.main.task;

import com.bzw.api.module.base.model.Order;
import com.bzw.api.module.base.model.OrderDetail;
import com.bzw.api.module.base.model.Room;
import com.bzw.api.module.main.biz.OrderEventBiz;
import com.bzw.api.module.main.biz.OrderQueryBiz;
import com.bzw.api.module.main.biz.RoomQueryBiz;
import com.bzw.api.module.main.enums.OrderDetailState;
import com.bzw.api.module.main.enums.OrderState;
import com.bzw.api.module.main.enums.RoomState;
import com.bzw.api.module.main.service.RoomEventService;
import com.bzw.api.module.main.service.TechnicianEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/*
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
    private RoomEventService roomEventService;



    @Scheduled(fixedDelay = ONE_Minute * 2, initialDelay = ONE_Minute)
    public void finishOrder() {
        Date now = new Date();
        List<Room> roomList = roomQueryBiz.listRoomByRoomSate(RoomState.unfinished);
        for (Room room : roomList) {
            if (now.compareTo(room.getOverTime()) >= 0) {
                List<OrderDetail> orderDetailList = orderQueryBiz.listOrderDetail(room.getOrderId());
                boolean isFinish = true;
                for (OrderDetail orderDetail : orderDetailList) {
                    if (orderDetail.getEndTime() != null && now.compareTo(orderDetail.getEndTime()) >= 0) {
                        orderDetail.setBizStatusId(OrderDetailState.finished.getValue());
                        technicianEventService.finishTechnician(orderDetail.getTechnicianId());
                        orderEventBiz.updateOrderDetail(orderDetail);
                    } else {
                        isFinish = false;
                    }
                }
                if (isFinish) {
                    Order order = orderQueryBiz.getOrder(room.getOrderId());
                    if (order != null && order.getBizStatusId().equals(OrderState.paid.getValue())) {
                        order.setBizStatusId(OrderState.finish.getValue());
                        order.setModifiedTime(now);
                        orderEventBiz.update(order);
                    }
                    roomEventService.freeRoom(null, room, now);
                }
            }
        }
    }

//    @Scheduled(fixedDelay = ONE_Minute)
//    public void finishMessage() {
//        Map<String, WebSocket> mapSocket = WebSocket.getWebSocketMap();
//        for (Map.Entry<String, WebSocket> entry : mapSocket.entrySet()) {
//            String id = entry.getKey();
//            messageEventService.sendMessageByKey(id);
//        }
//    }
}

