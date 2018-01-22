package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.dto.WechatAccesTokenDTO;
import com.bzw.api.module.basic.dto.WechatLoginDTO;
import com.bzw.api.module.basic.dto.WechatResult;
import com.bzw.api.module.basic.enums.WechatConstants;
import com.bzw.api.module.basic.param.WechatQrCodeParam;
import com.bzw.api.module.basic.param.WechatTemplateData;
import com.bzw.api.module.basic.param.WechatTemplatetMessageParam;
import com.bzw.common.cache.RedisClient;
import com.bzw.common.utils.HttpClient;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class WechatService {

    @Autowired
    private Gson gson;

    @Autowired
    private RedisClient redisClient;

    private static final String OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String GRCODE_URL = "http://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    private static final String TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s";

    public String getOpenId(String jscode) {
        String appid = WechatConstants.getAppid();
        String secret = WechatConstants.getSecret();
        String url = String.format(OPEN_ID_URL, appid, secret, jscode);
        String body = HttpClient.get(url);
        if (StringUtils.isNotBlank(body)) {
            WechatLoginDTO result = gson.fromJson(body, WechatLoginDTO.class);
            return result.getOpenid();
        } else {
            return null;
        }
    }

    public String getAccessToken() {
        String appid = WechatConstants.getAppid();
        String secret = WechatConstants.getSecret();
        String wechatkey = "wechat_accesstoken";
        String cacheValue = redisClient.get(wechatkey);
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        } else {
            String url = String.format(ACCESS_TOKEN_URL, appid, secret);
            String body = HttpClient.get(url);
            if (StringUtils.isNotBlank(body)) {
                WechatAccesTokenDTO result = gson.fromJson(body, WechatAccesTokenDTO.class);
                redisClient.set(wechatkey, result.getAccess_token(), result.getExpires_in());
                return result.getAccess_token();
            } else {
                return null;
            }
        }
    }

    public byte[] getGrCode(String page, String accessToken, String scene) {
        String url = String.format(GRCODE_URL, accessToken);
        WechatQrCodeParam param = new WechatQrCodeParam();
        param.setPage(page);
        param.setScene(scene);
        String paramContent = gson.toJson(param, WechatQrCodeParam.class);
        return HttpClient.postJson(url, paramContent);
    }

    public boolean sendTemplateMessage(String openId, String formId, String time, String amount, String projectName) {
        String accessToken = getAccessToken();
        String url = String.format(TEMPLATE_MESSAGE_URL, accessToken);
        WechatTemplatetMessageParam param = new WechatTemplatetMessageParam();
        param.setTemplate_id(WechatConstants.getBookId());
        param.setTouser(openId);
        param.setForm_id(formId);
        WechatTemplateData data = new WechatTemplateData();
        data.setKeyword1(time);
        data.setKeyword2(amount);
        data.setKeyword3(projectName);
        String paramConent = gson.toJson(param, WechatTemplatetMessageParam.class);
        String result = HttpClient.postJsonStr(url, paramConent);
        WechatResult wr = gson.fromJson(result, WechatResult.class);
        return wr.getErrcode() == 0;
    }

}
