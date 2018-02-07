package com.bzw.api.module.mq.service;

import com.bzw.api.module.base.model.Message;
import com.bzw.api.module.mq.biz.MessageEventBiz;
import com.bzw.api.module.mq.biz.MessageQueryBiz;
import com.bzw.api.module.mq.controller.WebSocket;
import com.bzw.api.module.mq.params.MessageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Service
public class MessageEventService {

    @Autowired
    private MessageQueryBiz messageQueryBiz;

    @Autowired
    private MessageEventBiz messageEventBiz;

    private final String SENDER = "SYSTEM";

    public void sendMessageByKey(String id) {
        List<Message> messageList = messageQueryBiz.listByReceiver(id);
        for (Message message : messageList) {
            String msg;
            if (StringUtils.isNotBlank(message.getJson())) {
                msg = message.getJson();
            } else {
                msg = message.getContent();
            }
            boolean isSend = WebSocket.sendMessage(message.getReceiver(), msg);
            if (isSend) {
                message.setIsFinish(Byte.parseByte("1"));
                message.setSendDate(new Date());
                messageEventBiz.update(message);
            }
        }
    }

    public void sendMessage(MessageParam messageParam) {
        boolean isSend = WebSocket.sendMessage(messageParam.getReceiver(), messageParam.getJson());
        String sender = messageParam.getSender();
        if (StringUtils.isBlank(sender)) {
            sender = SENDER;
        }
        messageEventBiz.add(sender, messageParam.getReceiver(), messageParam.getJson(), messageParam.getMessage(), isSend);
    }

}
