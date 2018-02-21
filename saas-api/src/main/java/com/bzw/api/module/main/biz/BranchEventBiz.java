package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.BranchMapper;
import com.bzw.api.module.base.model.Branch;
import com.bzw.common.system.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yanbin
 */
@Service
public class BranchEventBiz {

    @Autowired
    private BranchMapper branchMapper;

    public void add(Branch branch) {
        branchMapper.insert(branch);
    }

    public boolean update(Branch branch) {
        return branchMapper.updateByPrimaryKey(branch) > 0;
    }

    public boolean delete(Long id,Long employeeId) {
        Branch branch = branchMapper.selectByPrimaryKey(id);
        if (branch == null) {
            return false;
        }
        branch.setStatusId(Status.Delete.getValue());
        branch.setModifiedId(employeeId);
        branch.setModifiedTime(new Date());
        return branchMapper.updateByPrimaryKey(branch) > 0;
    }
}
