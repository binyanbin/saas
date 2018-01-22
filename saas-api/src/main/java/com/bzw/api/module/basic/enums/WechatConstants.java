package com.bzw.api.module.basic.enums;

import org.springframework.beans.factory.annotation.Value;

public class WechatConstants {

    @Value("${wechat.appid}")
    private static String appid;

    @Value("${wechat.secret}")
    private static String secret;

    @Value("${wechat.template.bookId")
    private static String bookId;

    @Value("${wechat.template.serveId")
    private static String serveId;

    public static String getAppid() {
        return appid;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getBookId() {
        return bookId;
    }

    public static String getServeId() {
        return serveId;
    }
}
