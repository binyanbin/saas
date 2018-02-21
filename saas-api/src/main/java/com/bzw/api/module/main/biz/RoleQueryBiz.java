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
public class RoleQueryBiz {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;


    public Role getRole(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public List<Role> listRole(){
        return roleMapper.selectByExample(null);
    }


    public List<RoleFunction> listRoleFunction(Integer roleId){
        RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
        roleFunctionExample.createCriteria().andRoleIdEqualTo(roleId);
        return roleFunctionMapper.selectByExample(roleFunctionExample);
    }

    public List<RoleFunction> listRoleFunction(){
        return roleFunctionMapper.selectByExample(null);
    }

}
