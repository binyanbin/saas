package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.*;
import com.bzw.api.module.main.biz.*;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.dto.IdName;
import com.bzw.api.module.main.dto.UserDTO;
import com.bzw.api.module.main.enums.FunctionId;
import com.bzw.api.module.main.enums.RoleType;
import com.bzw.api.module.main.params.UserParam;
import com.bzw.common.cache.CacheKeyPrefix;
import com.bzw.common.cache.ICacheClient;
import com.bzw.common.content.WebSession;
import com.bzw.common.content.WebSessionManager;
import com.bzw.common.system.Status;
import com.bzw.common.exception.api.PhoneNotExisitsException;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.exception.api.WechatLoginFailException;
import com.bzw.common.exception.api.WrongSmsCodeException;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Service
public class UserEventService {

    @Autowired
    private UserQueryBiz userQueryBiz;

    @Autowired
    private UserEventBiz userEventBiz;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private RoleQueryBiz roleQueryBiz;

    @Autowired
    private BranchQueryBiz branchQueryBiz;

    @Autowired
    private TenantQueryBiz tenantQueryBiz;

    @Autowired
    private EmployeeQueryBiz employeeQueryBiz;

    @Autowired
    private WebSessionManager webSessionManager;

    @Autowired
    private ICacheClient cacheClient;

    public WebSession login(String code, String password) {
        List<User> users = userQueryBiz.listLoginUser(code, password);
        if (CollectionUtils.isEmpty(users)) {
            throw new UserLoginFailException();
        }
        User user = users.get(0);
        List<Employee> employees = employeeQueryBiz.listEmployeeByUserId(user.getId());
        if (CollectionUtils.isEmpty(employees)) {
            throw new UserLoginFailException();
        }
        Employee employee = employees.get(0);
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());

        Branch branch = branchQueryBiz.getBranch(employee.getBranchId());
        if (branch != null) {
            webSession.setBranchName(branch.getName());
        }
        webSession.setOpenId(employee.getWechatId());
        Tenant tenant = tenantQueryBiz.getTenant(branch.getId());
        if (tenant != null) {
            webSession.setTenantName(tenant.getName());
        }

        user.setLastLoginDate(new Date());
        userEventBiz.updateUser(user);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public WebSession openIdLogin(String openId) {
        List<Employee> employees = employeeQueryBiz.listEmployeeByOpenId(openId);
        if (CollectionUtils.isEmpty(employees)) {
            throw new WechatLoginFailException();
        }
        Employee employee = employees.get(0);
        User user = userQueryBiz.getUser(employee.getUserId());
        if (user == null) {
            throw new WechatLoginFailException();
        }
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());
        Branch branch = branchQueryBiz.getBranch(employee.getBranchId());
        webSession.setBranchName(branch.getName());
        Tenant tenant = tenantQueryBiz.getTenant(branch.getTenantId());
        webSession.setTenantName(tenant.getName());
        user.setLastLoginDate(new Date());
        userEventBiz.updateUser(user);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public WebSession loginBySmsCode(String phone, String smsCode) {
        List<User> users = userQueryBiz.listUserByPhone(phone);
        if (CollectionUtils.isEmpty(users)) {
            throw new PhoneNotExisitsException();
        }
        String phoneKey = CacheKeyPrefix.mobile.getKey() + phone;
        if (cacheClient.hasKey(phoneKey)) {
            String code = cacheClient.get(phoneKey);
            if (!code.equals(smsCode)) {
                throw new WrongSmsCodeException();
            }
        } else {
            throw new WrongSmsCodeException();
        }
        User user = users.get(0);
        List<Employee> employees = employeeQueryBiz.listEmployeeByUserId(user.getId());
        if (CollectionUtils.isEmpty(employees)) {
            throw new UserLoginFailException();
        }
        Employee employee = employees.get(0);
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());
        Branch branch = branchQueryBiz.getBranch(employee.getBranchId());
        if (branch != null) {
            webSession.setBranchName(branch.getName());
        }
        webSession.setOpenId(employee.getWechatId());
        Tenant tenant = tenantQueryBiz.getTenant(branch.getId());
        if (tenant != null) {
            webSession.setTenantName(tenant.getName());
        }
        user.setLastLoginDate(new Date());
        userEventBiz.updateUser(user);
        cacheClient.delete(phoneKey);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public boolean bindOpenId(Long employeeId, String openId) {
        return userEventBiz.bindOpenId(employeeId, openId);
    }


    private static final String MAN = "男";

    public UserDTO add(UserParam userParam, Long employeeId, Long tenantId, Long branchId, String branchName) {
        Role role = roleQueryBiz.getRole(userParam.getRoleType());
        Preconditions.checkNotNull(role, WarnMessage.NOT_FOUND_ROLE);
        Date now = new Date();

        User user = new User();
        List<User> users = userQueryBiz.listUserByPhone(userParam.getPhone());
        if (CollectionUtils.isEmpty(users)) {
            user.setCreatedDate(now);
            user.setModifiedDate(now);
            user.setModifiedId(employeeId);
            user.setCreatedId(employeeId);
            user.setCode(userParam.getPhone());
            user.setName(userParam.getName());
            user.setCode(userParam.getPhone());
            user.setId(sequenceService.newKey(SeqType.User));
            user.setPassword("");
            user.setStatusId(Status.Valid.getValue());
            user.setWechatId("");
        } else {
            user = users.get(0);
        }
        userEventBiz.addUser(user);

        Employee employee = new Employee();
        if (MAN.equals(userParam.getSex())) {
            employee.setSex(MAN);
        } else {
            employee.setSex("女");
        }
        employee.setName(userParam.getName());
        employee.setPhone(userParam.getPhone());
        employee.setId(sequenceService.newKey(SeqType.employee));
        employee.setStatusId(Status.Valid.getValue());
        employee.setBranchId(branchId);
        employee.setBranchName(branchName);
        employee.setUserId(user.getId());
        employee.setTenantId(tenantId);
        employee.setWechatId("");
        employee.setRoleType(userParam.getRoleType());
        userEventBiz.addEmployee(employee);
        List<RoleFunction> roleFunctionList = roleQueryBiz.listRoleFunction(userParam.getRoleType());
        List<Long> employeeFunctionIds = sequenceService.newKeys(SeqType.employeeFunction, roleFunctionList.size());
        List<EmployeeFunction> employeeFunctions = Lists.newArrayList();
        for (int i = 0; i < roleFunctionList.size(); i++) {
            EmployeeFunction employeeFunction = new EmployeeFunction();
            employeeFunction.setId(employeeFunctionIds.get(i));
            employeeFunction.setBranchId(branchId);
            employeeFunction.setEmployeeId(employee.getId());
            employeeFunction.setFunctionId(roleFunctionList.get(i).getFunctionId());
            employeeFunction.setTenantId(tenantId);
            employeeFunctions.add(employeeFunction);
        }
        userEventBiz.addEmployeeFunction(employeeFunctions);
        return mapToUserDTO(employee, role, roleFunctionList);
    }

    public boolean update(Long employeeId, UserParam userParam) {
        Employee employee = userQueryBiz.getEmployee(employeeId);
        if (employee == null) {
            return false;
        } else {
            employee.setName(userParam.getName());
            if (MAN.equals(userParam.getSex())) {
                employee.setSex(MAN);
            } else {
                employee.setSex("女");
            }
            employee.setPhone(userParam.getPhone());
            return userEventBiz.updateEmployee(employee);
        }
    }

    public boolean delete(Long employeeId) {
        Employee employee = userQueryBiz.getEmployee(employeeId);
        if (employee == null) {
            return false;
        } else {
            employee.setStatusId(Status.Delete.getValue());
            return userEventBiz.updateEmployee(employee);
        }
    }

    public boolean updateUserPhone(Long userId, String phone, Long employeeId) {
        User user = userQueryBiz.getUser(userId);
        if (user == null) {
            return false;
        } else {
            user.setModifiedId(employeeId);
            user.setPhone(phone);
            user.setModifiedDate(new Date());
            return userEventBiz.updateUser(user);
        }
    }

    public UserDTO mapToUserDTO(Employee employee, Role role, List<RoleFunction> roleFunctions) {
        UserDTO result = new UserDTO();
        result.setId(employee.getId());
        result.setName(employee.getName());
        result.setPhone(employee.getPhone());
        result.setSex(employee.getSex());
        result.setRole(role.getName());
        List<IdName> functions = Lists.newArrayList();
        for (RoleFunction roleFunction : roleFunctions) {
            FunctionId functionId = FunctionId.parse(roleFunction.getFunctionId());
            if (functionId != null) {
                IdName idName = new IdName();
                idName.setName(functionId.getDesc());
                idName.setId(functionId.getValue());
                functions.add(idName);
            }
        }
        result.setFunctions(functions);
        return result;
    }

}
