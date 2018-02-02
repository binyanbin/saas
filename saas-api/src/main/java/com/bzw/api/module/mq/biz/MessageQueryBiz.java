package com.bzw.api.module.mq.biz;

import com.bzw.api.module.base.dao.MessageMapper;
import com.bzw.api.module.base.model.Message;
import com.bzw.api.module.base.model.MessageExample;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageQueryBiz {

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> listUnReceive(){
        Date now = new Date();
        MessageExample example = new MessageExample();
        Date begin = DateUtils.addDays(now,-1);
        example.createCriteria().andCreatedDateBetween(begin,now).andIsFinishEqualTo(Byte.parseByte("0"));
        return messageMapper.selectByExample(example);
    }

    public List<Message> listByReceiver(String receiver){
        Date now = new Date();
        MessageExample example = new MessageExample();
        Date begin = DateUtils.addDays(now,-1);
        example.createCriteria().andReceiverEqualTo(receiver).andCreatedDateBetween(begin,now).andIsFinishEqualTo(Byte.parseByte("0"));
        return messageMapper.selectByExample(example);
    }
}
