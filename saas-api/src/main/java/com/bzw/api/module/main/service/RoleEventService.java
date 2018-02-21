package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Role;
import com.bzw.api.module.base.model.RoleFunction;
import com.bzw.api.module.main.biz.RoleEventBiz;
import com.bzw.api.module.main.biz.RoleQueryBiz;
import com.bzw.api.module.main.dto.RoleDTO;
import com.bzw.api.module.main.enums.FunctionId;
import com.bzw.api.module.main.params.RoleParam;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleEventService {

    @Autowired
    private RoleEventBiz roleEventBiz;

    @Autowired
    private RoleQueryBiz roleQueryBiz;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private RoleQueryService roleQueryService;

    public RoleDTO add(RoleParam roleParam) {
        Role role = new Role();
        role.setId(sequenceService.newKey(SeqType.role).intValue());
        role.setDescription(roleParam.getDescription());
        role.setName(roleParam.getName());
        List<RoleFunction> roleFunctionList = mapToRoleFunction(roleParam.getFunctionIds(), role.getId());
        roleEventBiz.add(role);
        roleEventBiz.addFunction(roleFunctionList);
        return roleQueryService.mapToRoleDTO(role, roleFunctionList);
    }

    public boolean update(RoleParam roleParam, Integer id) {
        Role role = roleQueryBiz.getRole(id);
        if (role == null) {
            return false;
        } else {
            role.setId(sequenceService.newKey(SeqType.role).intValue());
            role.setDescription(roleParam.getDescription());
            role.setName(roleParam.getName());
            roleEventBiz.update(role);
            List<RoleFunction> roleFunctionList = mapToRoleFunction(roleParam.getFunctionIds(), role.getId());
            roleEventBiz.deleteFunction(role.getId());
            roleEventBiz.addFunction(roleFunctionList);
            return true;
        }
    }

    public boolean delete(Integer roleId) {
        return roleEventBiz.deleteRole(roleId);
    }

    private List<RoleFunction> mapToRoleFunction(List<Integer> functionIds, Integer roleId) {
        List<RoleFunction> roleFunctionList = Lists.newArrayList();
        List<Long> ids = sequenceService.newKeys(SeqType.roleFunction, functionIds.size());
        for (int i = 0; i < ids.size(); i++) {
            RoleFunction roleFunction = new RoleFunction();
            FunctionId functionId = FunctionId.parse(functionIds.get(i));
            if (functionId != null) {
                roleFunction.setId(ids.get(i).intValue());
                roleFunction.setFunctionId(functionIds.get(i));
                roleFunction.setRoleId(roleId);
                roleFunctionList.add(roleFunction);
            }
        }
        return roleFunctionList;
    }
}
