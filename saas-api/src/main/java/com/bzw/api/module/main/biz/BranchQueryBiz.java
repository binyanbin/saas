package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.BranchMapper;
import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.base.model.BranchExample;
import com.bzw.common.system.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Branch> listBranchByTenantId(Long tenantId){
        BranchExample example = new BranchExample();
        example.createCriteria().andTenantIdEqualTo(tenantId).andStatusIdEqualTo(Status.Valid.getValue());
        return branchMapper.selectByExample(example);
    }
}
