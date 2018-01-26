package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.constant.SmsConstants;
import com.bzw.api.module.basic.constant.ExternalUrl;
import com.bzw.common.cache.CacheKeyPrefix;
import com.bzw.common.cache.ICacheClient;
import com.bzw.common.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author yanbin
 */
@Service
public class SmsService {

    @Autowired
    private SmsConstants smsConstants;

    @Autowired
    private ICacheClient cacheClient;

    public Boolean sendSms(String phone) throws IOException, ParserConfigurationException, SAXException {
        String code = getRandomNum();
        String content = "验证码:" + code;
        String url = String.format(ExternalUrl.SMS_URL, smsConstants.getAccount(), smsConstants.getSecret(), smsConstants.getChanel(), content, phone);
        String result = HttpClient.get(url);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        assert result != null;
        InputStream in = new ByteArrayInputStream(result.getBytes("UTF-8"));
        org.w3c.dom.Document document = builder.parse(in);
        org.w3c.dom.Element element = document.getDocumentElement();
        Boolean res = "1".equals(element.getAttribute("result"));
        if (res) {
            cacheClient.set(CacheKeyPrefix.mobile.getKey() + phone, code, (int) CacheKeyPrefix.mobile.getTimeout());
        }
        return res;
    }

    private String getRandomNum() {
        return String.valueOf(100000 + new Random().nextInt(899999));
    }
}
