package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.RoleFunctionMapper;
import com.bzw.api.module.base.dao.RoleMapper;
import com.bzw.api.module.base.model.Role;
import com.bzw.api.module.base.model.RoleFunction;
import com.bzw.api.module.base.model.RoleFunctionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class RoleEventBiz {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    public void add(Role role) {
        roleMapper.insert(role);
    }

    public void addFunction(List<RoleFunction> roleFunctionList) {
        for (RoleFunction roleFunction : roleFunctionList) {
            roleFunctionMapper.insert(roleFunction);
        }
    }

    public boolean update(Role role) {
        return roleMapper.updateByPrimaryKey(role) > 0;
    }

    public void deleteFunction(Integer roleId) {
        RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
        roleFunctionExample.createCriteria().andRoleIdEqualTo(roleId);
        roleFunctionMapper.deleteByExample(roleFunctionExample);
    }

    public boolean deleteRole(Integer roleId){
        deleteFunction(roleId);
        return roleMapper.deleteByPrimaryKey(roleId)>0;
    }
}
