package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.constant.ExternalURL;
import com.bzw.api.module.basic.dto.WeChatAccessTokenDTO;
import com.bzw.api.module.basic.dto.WeChatLoginDTO;
import com.bzw.api.module.basic.dto.WeChatResult;
import com.bzw.api.module.basic.constant.WeChatConstants;
import com.bzw.api.module.basic.param.WeChatQrCodeParam;
import com.bzw.api.module.basic.param.WeChatTemplateData;
import com.bzw.api.module.basic.param.WeChatTemplateMessageParam;
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
public class WeChatService {

    @Autowired
    private Gson gson;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    WeChatConstants weChatConstants;


    public String getOpenId(String jscode) {
        String url = String.format(ExternalURL.OPEN_ID_URL, weChatConstants.getAppid(), weChatConstants.getSecret(), jscode);
        String body = HttpClient.get(url);
        if (StringUtils.isNotBlank(body)) {
            WeChatLoginDTO result = gson.fromJson(body, WeChatLoginDTO.class);
            return result.getOpenid();
        } else {
            return null;
        }
    }

    public String getAccessToken() {
        String cacheValue = redisClient.get(ExternalURL.WE_CHAT_KEY);
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        } else {
            String url = String.format(ExternalURL.ACCESS_TOKEN_URL, weChatConstants.getAppid(), weChatConstants.getSecret());
            String body = HttpClient.get(url);
            if (StringUtils.isNotBlank(body)) {
                WeChatAccessTokenDTO result = gson.fromJson(body, WeChatAccessTokenDTO.class);
                redisClient.set(ExternalURL.WE_CHAT_KEY, result.getAccess_token(), result.getExpires_in());
                return result.getAccess_token();
            } else {
                return null;
            }
        }
    }

    public byte[] getGrCode(String accessToken, String scene) {
        String url = String.format(ExternalURL.GR_CODE_URL, accessToken);
        WeChatQrCodeParam param = new WeChatQrCodeParam();
        param.setPage(ExternalURL.ROOM_PAGE);
        param.setScene(scene);
        String paramContent = gson.toJson(param, WeChatQrCodeParam.class);
        return HttpClient.postJson(url, paramContent);
    }

    public void sendTemplateMessage(String openId, String formId, String time, String amount, String projectName,String queryString) {
        String accessToken = getAccessToken();
        String url = String.format(ExternalURL.TEMPLATE_MESSAGE_URL, accessToken);
        WeChatTemplateMessageParam param = new WeChatTemplateMessageParam();
        param.setTemplate_id(weChatConstants.getBookId());
        param.setTouser(openId);
        param.setForm_id(formId);
        param.setPage(ExternalURL.TECHNICIAN_MESSAGE_PAGE);
        WeChatTemplateData data = new WeChatTemplateData();
        data.setKeyword1(time);
        data.setKeyword2(amount);
        data.setKeyword3(projectName);
        String paramContent = gson.toJson(param, WeChatTemplateMessageParam.class);
        String result = HttpClient.postJsonStr(url, paramContent);
        gson.fromJson(result, WeChatResult.class);
    }
}
