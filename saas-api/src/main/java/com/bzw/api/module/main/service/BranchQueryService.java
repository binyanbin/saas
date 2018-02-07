package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.main.biz.BranchQueryBiz;
import com.bzw.api.module.main.biz.RoomQueryBiz;
import com.bzw.api.module.main.dto.BranchDTO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class BranchQueryService {

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private BranchQueryBiz branchQueryBiz;

    public BranchDTO getBranchByRoomId(Long roomId) {
        Branch branch = roomQueryBiz.getBranchByRoomId(roomId);
        if (branch == null) {
            return null;
        } else {
            BranchDTO result = mapToBranchDTO(branch);
            return result;
        }
    }

    public BranchDTO mapToBranchDTO(Branch branch) {
        BranchDTO result = new BranchDTO();
        result.setId(branch.getId());
        result.setAddress(branch.getAddress());
        result.setName(branch.getName());
        result.setTelephone(branch.getTelephone());
        return result;
    }

    public List<BranchDTO> listBranchByTenantId(Long tenantId){
        List<BranchDTO> result = Lists.newArrayList();
        List<Branch> branches = branchQueryBiz.listBranchByTenantId(tenantId);
        for (Branch branch :branches){
            result.add(mapToBranchDTO(branch));
        }
        return result;
    }

    public BranchDTO getBranch(Long id){
        return mapToBranchDTO(branchQueryBiz.getBranch(id));
    }
}
