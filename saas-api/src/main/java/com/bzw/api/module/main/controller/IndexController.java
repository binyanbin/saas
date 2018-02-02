package com.bzw.api.module.main.controller;

import com.bzw.api.module.third.service.BaiduService;
import com.bzw.common.content.ApiMethodAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yanbin
 */
@RestController
public class IndexController {

    @Value("${application.title}")
    private String title;

    @Value("${application.version}")
    private String version;

    @Autowired
    private BaiduService baiduService;

    @RequestMapping("/")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object index() {
        return title + " verison:" + version;
    }

    @RequestMapping(value = "/voice")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public void getVoice(HttpServletResponse response, @RequestParam String voice) throws IOException {
        response.setContentType("audio/mp3");
        OutputStream stream = response.getOutputStream();
        stream.write(baiduService.getVoice(voice));
        stream.flush();
        stream.close();
    }
}
