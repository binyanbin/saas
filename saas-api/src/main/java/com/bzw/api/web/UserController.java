package com.bzw.api.web;

import com.bzw.api.module.basic.param.LoginParam;
import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.api.module.basic.service.SmsService;
import com.bzw.api.module.basic.service.WeChatService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private CustomerEventService customerEventService;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/{phone}/smscode", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getSmsCode(@PathVariable String phone) throws ParserConfigurationException, SAXException, IOException {
        Preconditions.checkArgument(customerQueryService.containPhone(phone), "系统不存在该手机号");
        return wrapperJsonView(smsService.sendSms(phone));
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
        String openId = weChatService.getOpenId(jscode);
        if (null == openId) {
            throw new UserLoginFailException();
        }
        return wrapperJsonView(customerEventService.openIdLogin(openId));
    }

    @RequestMapping(value = "openId/{jscode}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object accessToken(@PathVariable String jscode) {
        return wrapperJsonView(weChatService.getOpenId(jscode));
    }


    @RequestMapping(value = "openId/{openId}/orders")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listOrderByOpenId(@PathVariable String openId) {
        return wrapperJsonView(customerQueryService.listOrderByOpenId(openId));
    }

    @RequestMapping(value = "/{userId}/orders")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object listOrderByUserId(@PathVariable Long userId) {
        return wrapperJsonView(customerQueryService.listOrderByUserId(userId));
    }

    @RequestMapping(value = "/bind/{openId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object bindUser(@PathVariable String openId) {
        return wrapperJsonView(customerEventService.bindOpenId(WebUtils.Session.getEmployeeId(), openId));
    }

}
