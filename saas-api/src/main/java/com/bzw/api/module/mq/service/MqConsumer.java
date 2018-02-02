package com.bzw.api.module.mq.service;

import com.bzw.api.module.mq.constants.MqTags;
import com.bzw.api.module.mq.params.MessageParam;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author yanbin
 */
@Component
public class MqConsumer {

    @Autowired
    private MessageEventService messageEventService;

    @Autowired
    private Gson gson;

    private final int SLEEP_TIME = 3000;

    @JmsListener(destination = MqTags.WEBSOCKET_CONNECT)
    public void webSocketConnected(String id) throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        messageEventService.sendMessageByKey(id);
    }

    @JmsListener(destination = MqTags.WEBSOCKET_SEND)
    public void webSocketSend(String msg) {
        MessageParam messageParam =gson.fromJson(msg,MessageParam.class);
        messageEventService.sendMessage(messageParam);
    }

}
