package com.bzw.api.module.third.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class SmsConstants {

    @Value("${sms.account}")
    private String account;

    @Value("${sms.secret}")
    private String secret;

    @Value("${sms.chanel}")
    private String chanel;

    private final String smsCode = "验证码:%S";

    public String getAccount() {
        return account;
    }

    public String getSecret() {
        return secret;
    }

    public String getChanel() {
        return chanel;
    }

    public String getSmsCode() {
        return smsCode;
    }
}
