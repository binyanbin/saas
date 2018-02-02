package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.BranchMapper;
import com.bzw.api.module.base.model.Branch;
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
