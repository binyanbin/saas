package com.bzw.api.module.basic.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsConstants {

    @Value("${sms.account}")
    private String account;

    @Value("${sms.secret}")
    private String secret;

    @Value("${sms.chanel}")
    private String chanel;

    public String getAccount() {
        return account;
    }

    public String getSecret() {
        return secret;
    }

    public String getChanel() {
        return chanel;
    }
}
