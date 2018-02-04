package com.bzw.api.module.third.service;

import com.bzw.api.module.base.model.Sms;
import com.bzw.api.module.main.constant.ExternalUrl;
import com.bzw.api.module.third.biz.SmsEventBiz;
import com.bzw.api.module.third.constants.SmsConstants;
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

    @Autowired
    private SmsEventBiz smsEventBiz;

    public Boolean sendSms(String phone) throws ParserConfigurationException, SAXException, IOException {
        return sendSms(phone,0L,0L);
    }

    public Boolean sendSms(String phone, Long tenantId, Long branchId) throws IOException, ParserConfigurationException, SAXException {
        String code = getRandomNum();
        String content = String.format(smsConstants.getSmsCode(), code);
        Sms sms = smsEventBiz.add(phone, tenantId, branchId, content);
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
            smsEventBiz.finish(sms.getId());
        }
        return res;
    }

    private String getRandomNum() {
        return String.valueOf(100000 + new Random().nextInt(899999));
    }
}
