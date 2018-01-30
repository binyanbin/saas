package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.constant.MqTags;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

@Service
public class MqProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void websocketConnected(final String key) {
        Destination destination = new ActiveMQQueue(MqTags.WEBSOCKET_CONNECT);
        jmsTemplate.convertAndSend(destination, key);
    }
}
