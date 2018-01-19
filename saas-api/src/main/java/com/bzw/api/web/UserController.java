package com.bzw.api.web;

import com.bzw.api.module.basic.param.LoginParam;
import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.api.module.basic.service.WechatService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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
    private WechatService wechatService;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

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

    @RequestMapping(value = "/login/code/{jscode}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object codeLogin(@PathVariable String jscode) {
        String openId = wechatService.getOpenId(appid, secret, jscode);
        if (null == openId) {
            throw new UserLoginFailException();
        }
        return wrapperJsonView(customerEventService.openIdLogin(openId));
    }

    @RequestMapping(value="wechat/accesstoken")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object accessToken(){
        return wrapperJsonView(wechatService.getAccessToken(appid,secret));
    }



    @RequestMapping(value="wechat/openId/{jscode}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getOpenId(@PathVariable String jscode) {
        return wrapperJsonView(wechatService.getOpenId(appid, secret, jscode));
    }

    @RequestMapping(value = "/{userId}/orders")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object modify(@PathVariable Long userId) {
        return wrapperJsonView(customerQueryService.listOrderByUserId(userId));
    }

    @RequestMapping(value = "/bind/{openId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object bindUser(@PathVariable String openId) {
        return wrapperJsonView(customerEventService.bindOpenId(WebUtils.Session.getEmployeeId(), openId));
    }

}
