package com.bzw.api.module.mq.controller;

import com.bzw.api.module.mq.service.MqProducer;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("mq")
public class MqController extends BaseController {
    @Autowired
    MqProducer mqProducer;

    @RequestMapping(value = "/push")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object pushWebSocket(@RequestParam String receiver) {
        mqProducer.connected(receiver);
        return wrapperJsonView(true);
    }
}
