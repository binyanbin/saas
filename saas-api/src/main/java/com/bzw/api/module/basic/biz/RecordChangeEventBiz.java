package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.RecordChangeMapper;
import com.bzw.api.module.basic.enums.RecordChangeType;
import com.bzw.api.module.basic.model.RecordChange;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yanbin
 */
@Service
public class RecordChangeEventBiz {

    @Autowired
    private RecordChangeMapper recordChangeMapper;

    @Autowired
    private SequenceService sequenceService;

    public void add(RecordChangeType type,Long documentId,Long tenantId,Date now,String content){
        RecordChange recordChange = new RecordChange();
        recordChange.setType(type.getValue());
        recordChange.setTenantId(tenantId);
        recordChange.setDocumentId(documentId);
        recordChange.setChnageDate(now);
        recordChange.setId(sequenceService.newKey(SeqType.recordChange));
        recordChange.setContent(content);
        add(recordChange);
    }

    public void add(RecordChange recordChange){
        recordChangeMapper.insert(recordChange);
    }
}
