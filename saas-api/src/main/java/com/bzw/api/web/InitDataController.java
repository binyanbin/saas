package com.bzw.api.web;

import com.bzw.api.module.platform.service.InitService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}