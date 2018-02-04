package com.bzw.api.module.main.controller;

import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.model.User;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.params.LoginParam;
import com.bzw.api.module.main.service.CustomerQueryService;
import com.bzw.api.module.main.service.OrderQueryService;
import com.bzw.api.module.third.service.BaiduService;
import com.bzw.api.module.third.service.SmsService;
import com.bzw.api.module.third.service.WcService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private BaiduService.CustomerEventService customerEventService;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private WcService wcService;

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/{phone}/smscode", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getSmsCode(@PathVariable String phone) throws ParserConfigurationException, SAXException, IOException {
        List<User> users = customerQueryService.listUsersByPhone(phone);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(users), WarnMessage.NOT_EXISTS_PHONE);
        Employee employee = customerQueryService.getEmployeeByUsers(users);
        if (employee != null) {
            return wrapperJsonView(smsService.sendSms(phone, employee.getTenantId(), employee.getBranchId()));
        } else {
            return wrapperJsonView(smsService.sendSms(phone));
        }
    }

    @RequestMapping(value = "/login", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object login(@RequestBody LoginParam loginParam) {
        return wrapperJsonView(customerEventService.login(loginParam.getCode(), loginParam.getPassword()));
    }

    @RequestMapping(value = "/login/{openId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object openIdLogin(@PathVariable String openId) {
        return wrapperJsonView(customerEventService.openIdLogin(openId));
    }

    @RequestMapping(value = "/login/{phone}/{smsCode}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object smsCodeLogin(@PathVariable String phone, @PathVariable String smsCode) {
        return wrapperJsonView(customerEventService.loginBySmsCode(phone, smsCode));
    }

    @RequestMapping(value = "/login/code/{jscode}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object codeLogin(@PathVariable String jscode) {
        String openId = wcService.getOpenId(jscode);
        if (null == openId) {
            throw new UserLoginFailException();
        }
        return wrapperJsonView(customerEventService.openIdLogin(openId));
    }

    @RequestMapping(value = "openId/{jscode}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object accessToken(@PathVariable String jscode) {
        return wrapperJsonView(wcService.getOpenId(jscode));
    }


    @RequestMapping(value = "openId/{openId}/orders")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listOrderByOpenId(@PathVariable String openId) {
        return wrapperJsonView(orderQueryService.listOrderByOpenId(openId));
    }

    @RequestMapping(value = "/{userId}/orders")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object listOrderByUserId(@PathVariable Long userId) {
        return wrapperJsonView(orderQueryService.listOrderByUserId(userId));
    }

    @RequestMapping(value = "/bind/{openId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object bindUser(@PathVariable String openId) {
        return wrapperJsonView(customerEventService.bindOpenId(WebUtils.Session.getEmployeeId(), openId));
    }

}
