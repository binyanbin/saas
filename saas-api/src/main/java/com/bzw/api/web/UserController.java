package com.bzw.api.web;

import com.bzw.api.module.basic.param.LoginParam;
import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.exception.api.LoginFailException;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private CustomerEventService customerEventService;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @RequestMapping(value = "/login", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object Login(@RequestBody LoginParam loginParam) {
        return wrapperJsonView(customerEventService.login(loginParam.getCode(), loginParam.getPassword()));
    }

    @RequestMapping(value = "/login/{openId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object openIdLogin(@PathVariable String openId) {
        return wrapperJsonView(customerEventService.openIdLogin(openId));
    }

    @RequestMapping(value = "/login/code/{code}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object codeLogin(@PathVariable String code) {
        String openId = customerEventService.getOpenId(appid, secret, code);
        if (null == openId)
            throw new LoginFailException();
        return wrapperJsonView(customerEventService.openIdLogin(openId));
    }

    @RequestMapping(value="wechat/accesstoken")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object accessToken(){
        return wrapperJsonView(customerEventService.getAccessToken(appid,secret));
    }

    @RequestMapping(value="wechat/qrcode/{roomId}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public void QRcode(@PathVariable Long roomId, HttpServletResponse response) throws IOException {
        String accessToken = customerEventService.getAccessToken(appid,secret);
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(customerEventService.getGrCode("/user/page/index",accessToken,"roomId="+roomId.toString()));
        stream.flush();
        stream.close();
    }


    @RequestMapping(value = "/bind/{openId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object bindUser(@PathVariable String openId) {
        return wrapperJsonView(customerEventService.bindOpenId(WebUtils.Session.getEmployeeId(), openId));
    }


}
