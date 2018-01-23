package com.bzw.api.web;

import com.bzw.api.module.basic.service.InitService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("data")
public class InitDataController extends BaseController {

    @Autowired
    private InitService initService;

    @RequestMapping("/init")
    @ApiMethodAttribute(nonSessionValidation = true,nonSignatureValidation = true)
    public Object test() {
        initService.initUser();
        return wrapperJsonView(true);
    }

    @RequestMapping("/sendMessage/{id}")
    @ApiMethodAttribute(nonSessionValidation = true,nonSignatureValidation = true)
    public Object sendMessage(@PathVariable String id) throws IOException {
        WebSocket.sendMessage(id,"测试");
        return wrapperJsonView(true);
    }

}
