package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.MessageEventBiz;
import com.bzw.api.module.basic.biz.MessageQueryBiz;
import com.bzw.api.module.basic.constant.MqTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MqConsumer {

    @Autowired
    private MessageQueryBiz messageQueryBiz;

    @Autowired
    private MessageEventBiz messageEventBiz;

    @JmsListener(destination = MqTags.WEBSOCKET_CONNECT)
    public void webSocketConnected(String id) throws InterruptedException {
        return;
    }

}
