package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.RecordChangeMapper;
import com.bzw.api.module.basic.model.RecordChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class RecordChangeEventBiz {

    @Autowired
    private RecordChangeMapper recordChangeMapper;

    public void add(RecordChange recordChange){
        recordChangeMapper.insert(recordChange);
    }
}
