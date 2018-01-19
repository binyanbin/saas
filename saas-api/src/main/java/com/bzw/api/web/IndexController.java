package com.bzw.api.web;

import com.bzw.common.content.ApiMethodAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author yanbin
 */
@RestController
public class IndexController {

    @Value("${application.title}")
    private String title;

    @Value("${application.version}")
    private String version;

    @RequestMapping("/")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object index() {
        return title + " verison:" + version;
    }
}
