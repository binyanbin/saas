package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.BranchMapper;
import com.bzw.api.module.basic.model.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class BranchQueryBiz {

    @Autowired
    private BranchMapper branchMapper;

    public Branch getBranch(Long id){
        return branchMapper.selectByPrimaryKey(id);
    }
}
