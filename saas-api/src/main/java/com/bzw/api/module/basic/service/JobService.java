package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.OrderDetailState;
import com.bzw.api.module.basic.enums.OrderState;
import com.bzw.api.module.basic.enums.RoomState;
import com.bzw.api.module.basic.model.Message;
import com.bzw.api.module.basic.model.Order;
import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.Room;
import com.bzw.api.web.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private MessageQueryBiz messageQueryBiz;

    @Autowired
    private MessageEventBiz messageEventBiz;

    @Scheduled(fixedDelay = ONE_Minute * 2, initialDelay = ONE_Minute)
    public void finishOrder() {
        Date now = new Date();
        List<Room> roomList = roomQueryBiz.listRoomByRoomSate(RoomState.unfinished);
        for (Room room : roomList) {
            if (now.compareTo(room.getOverTime()) >= 0) {
                List<OrderDetail> orderDetailList = orderQueryBiz.listOrderDetail(room.getOrderId());
                boolean isFinish = true;
                for (OrderDetail orderDetail : orderDetailList) {
                    if (now.compareTo(orderDetail.getEndTime()) >= 0) {
                        orderDetail.setBizStatusId(OrderDetailState.finished.getValue());
                        technicianEventService.finishTechnician(orderDetail.getTechnicianId());
                        orderEventBiz.updateOrderDetail(orderDetail);
                    } else {
                        isFinish = false;
                    }
                }
                if (isFinish) {
                    Order order = orderQueryBiz.getOrder(room.getOrderId());
                    if (order.getBizStatusId().equals(OrderState.paid.getValue())) {
                        order.setBizStatusId(OrderState.finish.getValue());
                        order.setModifiedTime(now);
                        orderEventBiz.update(order);
                    }
                    roomEventService.freeRoom(null, room, now);
                }
            }
        }
    }

    @Scheduled(fixedDelay = ONE_Minute )
    public void finishMessage(){
        Map<String,WebSocket> mapWebsocket = WebSocket.getWebSocketMap();
        for(Map.Entry<String,WebSocket> entry :mapWebsocket.entrySet()) {
            String id = entry.getKey();
            List<Message> messageList = messageQueryBiz.listByReceiver(id);
            for (Message message : messageList) {
                boolean isSend = WebSocket.sendMessage(message.getReceiver(), message.getJson());
                if (isSend) {
                    message.setIsFinish(Byte.parseByte("1"));
                    messageEventBiz.update(message);
                }
            }
        }
    }
}

