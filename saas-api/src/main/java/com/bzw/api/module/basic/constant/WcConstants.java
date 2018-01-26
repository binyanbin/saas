package com.bzw.api.module.basic.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class WcConstants {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.template.bookId}")
    private String bookId;

    @Value("${wechat.template.serveId}")
    private String serveId;


    public String getAppid() {
        return appid;
    }

    public String getSecret() {
        return secret;
    }

    public String getBookId() {
        return bookId;
    }

    public String getServeId() {
        return serveId;
    }
}
