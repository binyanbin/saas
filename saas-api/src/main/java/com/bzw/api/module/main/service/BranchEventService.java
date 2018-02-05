package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.main.biz.BranchEventBiz;
import com.bzw.api.module.main.biz.BranchQueryBiz;
import com.bzw.api.module.main.dto.BranchDTO;
import com.bzw.api.module.main.params.BranchParam;
import com.bzw.common.enums.Status;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BranchEventService {

    @Autowired
    private BranchEventBiz branchEventBiz;

    @Autowired
    private BranchQueryBiz branchQueryBiz;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private BranchQueryService branchQueryService;

    public BranchDTO add(BranchParam branchParam, Long tenantId, Long employeeId) {
        Date now = new Date();
        Branch branch = new Branch();
        branch.setId(sequenceService.newKey(SeqType.branch));
        branch.setCreatedId(employeeId);
        branch.setCreatedTime(now);
        branch.setModifiedId(employeeId);
        branch.setModifiedTime(now);
        branch.setStatusId(Status.Valid.getValue());
        branch.setTenantId(tenantId);
        branch.setAddress(branchParam.getAddress());
        branch.setName(branchParam.getName());
        branch.setTelephone(branchParam.getPhone());
        branchEventBiz.add(branch);
        return branchQueryService.mapToBranchDTO(branch);
    }

    public boolean update(BranchParam branchParam, Long branchId, Long employeeId) {
        Branch branch = branchQueryBiz.getBranch(branchId);
        Date now = new Date();
        if (branch == null) {
            return false;
        }
        branch.setModifiedId(employeeId);
        branch.setModifiedTime(now);
        branch.setAddress(branchParam.getAddress());
        branch.setName(branchParam.getName());
        branch.setTelephone(branchParam.getPhone());
        return branchEventBiz.update(branch);
    }

    public boolean delete(Long id) {
        return branchEventBiz.delete(id);
    }
}
