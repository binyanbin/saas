package com.bzw.api.module.third.biz;

import com.bzw.api.module.base.dao.SmsMapper;
import com.bzw.api.module.base.model.Sms;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsEventBiz {

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private SequenceService sequenceService;

    public Sms add(String phone, Long tenantId,Long branchId,String content) {
        Sms sms = new Sms();
        sms.setCreatedDate(new Date());
        sms.setId(sequenceService.newKey(SeqType.sms));
        sms.setPhone(phone);
        sms.setIsFinish(Byte.parseByte("0"));
        sms.setTenantId(tenantId);
        sms.setContent(content);
        sms.setBranchId(branchId);
        smsMapper.insert(sms);
        return sms;
    }

    public void finish(Long id){
        Sms sms = smsMapper.selectByPrimaryKey(id);
        sms.setFinishDate(new Date());
        sms.setIsFinish(Byte.parseByte("1"));
        smsMapper.updateByPrimaryKeySelective(sms);
    }
}
