package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.BranchMapper;
import com.bzw.api.module.basic.dao.EmployeeMapper;
import com.bzw.api.module.basic.dao.TenantMapper;
import com.bzw.api.module.basic.enums.RoleType;
import com.bzw.api.module.basic.model.Branch;
import com.bzw.api.module.basic.model.Employee;
import com.bzw.api.module.basic.model.EmployeeExample;
import com.bzw.api.module.basic.model.Tenant;
import com.bzw.api.module.platform.dao.UserMapper;
import com.bzw.api.module.platform.model.User;
import com.bzw.api.module.platform.model.UserExample;
import com.bzw.common.content.WebSession;
import com.bzw.common.content.WebSessionManager;
import com.bzw.common.enums.Status;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.exception.api.WechatLoginFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserEventBiz {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private WebSessionManager webSessionManager;

    public WebSession Login(String code, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCodeEqualTo(code).andPasswordEqualTo(password).andStatusIdEqualTo(Status.Valid.getValue());
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users))
            throw new UserLoginFailException();
        User user = users.get(0);
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andUserIdEqualTo(user.getId()).andStatusIdEqualTo(Status.Valid.getValue());
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        if (CollectionUtils.isEmpty(employees))
            throw new UserLoginFailException();
        Employee employee = employees.get(0);
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());
        Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchId());
        webSession.setBranchName(branch.getName());
        Tenant tenant = tenantMapper.selectByPrimaryKey(employee.getTenantId());
        webSession.setTenantName(tenant.getName());
        webSession.setOpenId(employee.getWechatId());
        user.setLastLoginDate(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public WebSession openIdLogin(String openId){
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andWechatIdEqualTo(openId).andStatusIdEqualTo(Status.Valid.getValue());
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        if (CollectionUtils.isEmpty(employees))
            throw new WechatLoginFailException();
        Employee employee = employees.get(0);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(employee.getUserId()).andStatusIdEqualTo(Status.Valid.getValue());
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users))
            throw new WechatLoginFailException();
        User user = users.get(0);
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());
        Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchId());
        webSession.setBranchName(branch.getName());
        Tenant tenant = tenantMapper.selectByPrimaryKey(employee.getTenantId());
        webSession.setTenantName(tenant.getName());
        user.setLastLoginDate(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public boolean bindOpenId(Long employeeId, String openId){
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        employee.setWechatId(openId);
        return employeeMapper.updateByPrimaryKeySelective(employee)>0;
    }
}
