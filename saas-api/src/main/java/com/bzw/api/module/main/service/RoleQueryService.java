package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Role;
import com.bzw.api.module.base.model.RoleFunction;
import com.bzw.api.module.main.biz.RoleQueryBiz;
import com.bzw.api.module.main.dto.IdName;
import com.bzw.api.module.main.dto.RoleDTO;
import com.bzw.api.module.main.enums.FunctionId;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class RoleQueryService {

    @Autowired
    private RoleQueryBiz roleQueryBiz;

    public List<RoleDTO> listRoles() {
        List<Role> roles = roleQueryBiz.listRole();
        List<RoleFunction> roleFunctions = roleQueryBiz.listRoleFunction();
        Map<Integer, List<RoleFunction>> mapRoleFunction = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(roleFunctions)) {
            roleFunctions.forEach(t -> {
                if (mapRoleFunction.containsKey(t.getRoleId())) {
                    List<RoleFunction> roleFunctionList = mapRoleFunction.get(t.getRoleId());
                    roleFunctionList.add(t);
                } else {
                    List<RoleFunction> roleFunctionList = Lists.newArrayList();
                    roleFunctionList.add(t);
                    mapRoleFunction.put(t.getRoleId(), roleFunctionList);
                }
            });
        }
        List<RoleDTO> result = Lists.newArrayList();
        for (Role role : roles) {
            List<RoleFunction> roleFunctionList = mapRoleFunction.get(role.getId());
            result.add(mapToRoleDTO(role, roleFunctionList));
        }
        return result;
    }

    public RoleDTO getRole(Integer id) {
        Role role = roleQueryBiz.getRole(id);
        List<RoleFunction> roleFunctionList = roleQueryBiz.listRoleFunction(id);
        return mapToRoleDTO(role, roleFunctionList);
    }

    public RoleDTO mapToRoleDTO(Role role, List<RoleFunction> roleFunctions) {
        RoleDTO result = new RoleDTO();
        result.setId(role.getId());
        result.setDescription(role.getDescription());
        result.setName(role.getName());
        List<IdName> functions = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(roleFunctions)) {
            for (RoleFunction roleFunction : roleFunctions) {
                IdName idName = new IdName();
                idName.setId(roleFunction.getFunctionId());
                idName.setName(FunctionId.parse(roleFunction.getFunctionId()).getDesc());
                functions.add(idName);
            }
            result.setFunctions(functions);
        }
        return result;
    }
}
