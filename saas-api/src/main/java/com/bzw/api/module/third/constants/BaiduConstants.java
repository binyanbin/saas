package com.bzw.api.module.third.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yanbin
 */
@Component
public class BaiduConstants {

    @Value("${baidu.appid}")
    private String appId;
    @Value("${baidu.appkey}")
    private String appKey;
    @Value("${baidu.secret}")
    private String secret;

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getSecret() {
        return secret;
    }
}
