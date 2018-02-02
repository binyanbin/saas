package com.bzw.api.module.mq.service;

import com.bzw.api.module.mq.constants.MqTags;
import com.bzw.api.module.mq.params.MessageParam;
import com.google.gson.Gson;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author yanbin
 */
@Service
public class MqProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;
    @Autowired
    private Gson gson;

    public void connected(final String key) {
        Destination destination = new ActiveMQQueue(MqTags.WEBSOCKET_CONNECT);
        jmsTemplate.convertAndSend(destination, key);
    }

    public void send(final MessageParam messageParam) {
        Destination destination = new ActiveMQQueue(MqTags.WEBSOCKET_SEND);
        String msg = gson.toJson(messageParam,MessageParam.class);
        jmsTemplate.convertAndSend(destination, msg);
    }
}
