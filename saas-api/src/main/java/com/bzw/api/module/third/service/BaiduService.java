package com.bzw.api.module.third.service;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.bzw.api.module.main.biz.*;
import com.bzw.api.module.main.enums.RoleType;
import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.model.Tenant;
import com.bzw.api.module.base.model.User;
import com.bzw.api.module.third.constants.BaiduConstants;
import com.bzw.common.cache.CacheKeyPrefix;
import com.bzw.common.cache.ICacheClient;
import com.bzw.common.content.WebSession;
import com.bzw.common.content.WebSessionManager;
import com.bzw.common.exception.api.PhoneNotExisitsException;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.exception.api.WechatLoginFailException;
import com.bzw.common.exception.api.WrongSmsCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class BaiduService {

    @Autowired
    private BaiduConstants baiduConstants;

    public byte[] getVoice(String voice) {
        AipSpeech client = new AipSpeech(baiduConstants.getAppId(), baiduConstants.getAppKey(), baiduConstants.getSecret());
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        TtsResponse res = client.synthesis(voice, "zh", 1, null);
        byte[] data = res.getData();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "output.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;

    }

    /**
     * @author yanbin
     */
    @Service
    public static class CustomerEventService {

        @Autowired
        private BranchQueryBiz branchQueryBiz;

        @Autowired
        private TenantQueryBiz tenantQueryBiz;

        @Autowired
        private UserEventBiz userEventBiz;

        @Autowired
        private UserQueryBiz userQueryBiz;

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
    }
}
