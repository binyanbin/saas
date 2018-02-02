package com.bzw.api.module.mq.biz;

import com.bzw.api.module.base.dao.MessageMapper;
import com.bzw.api.module.base.model.Message;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageEventBiz {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SequenceService sequenceService;

    public void add(String sender,String receiver,String json,String content,boolean isFinish){
        Date now = new Date();
        Message message = new Message();
        message.setId(sequenceService.newKey(SeqType.message));
        message.setContent(content);
        message.setCreatedDate(now);
        message.setJson(json);
        message.setSender(sender);
        message.setReceiver(receiver);
        if (isFinish) {
            message.setIsFinish(Byte.parseByte("1"));
            message.setSendDate(new Date());
        }
        else {
            message.setIsFinish(Byte.parseByte("0"));
        }
        messageMapper.insert(message);
    }

    public void update(Message message){
        messageMapper.updateByPrimaryKey(message);
    }
}
