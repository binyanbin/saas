package com.bzw.api.web;

import com.bzw.common.content.ApiMethodAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PushController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("listen/{userId}")
    @ApiMethodAttribute(nonSignatureValidation = true,nonSessionValidation = true)
    public Object listen(@PathVariable Long userId) {
        messagingTemplate.convertAndSend("listen/" + userId.toString(), "ready");
        return null;
    }
}
