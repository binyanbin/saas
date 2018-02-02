package com.bzw.api.module.third.service;

import com.bzw.api.module.main.constant.ExternalUrl;
import com.bzw.api.module.third.dto.WsAccessTokenDTO;
import com.bzw.api.module.third.dto.WsLoginDTO;
import com.bzw.api.module.third.dto.WsResult;
import com.bzw.api.module.third.constants.WcConstants;
import com.bzw.api.module.third.params.WcQrCodeParam;
import com.bzw.api.module.third.params.WcTemplateData;
import com.bzw.api.module.third.params.WcTemplateMessageParam;
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
public class WcService {

    @Autowired
    private Gson gson;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    WcConstants wcConstants;

    public String getOpenId(String jscode) {
        String url = String.format(ExternalUrl.OPEN_ID_URL, wcConstants.getAppid(), wcConstants.getSecret(), jscode);
        String body = HttpClient.get(url);
        if (StringUtils.isNotBlank(body)) {
            WsLoginDTO result = gson.fromJson(body, WsLoginDTO.class);
            return result.getOpenid();
        } else {
            return null;
        }
    }

    public String getAccessToken() {
        String cacheValue = redisClient.get(ExternalUrl.WE_CHAT_KEY);
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        } else {
            String url = String.format(ExternalUrl.ACCESS_TOKEN_URL, wcConstants.getAppid(), wcConstants.getSecret());
            String body = HttpClient.get(url);
            if (StringUtils.isNotBlank(body)) {
                WsAccessTokenDTO result = gson.fromJson(body, WsAccessTokenDTO.class);
                redisClient.set(ExternalUrl.WE_CHAT_KEY, result.getAccess_token(), result.getExpires_in());
                return result.getAccess_token();
            } else {
                return null;
            }
        }
    }

    public byte[] getGrCode(String accessToken, String scene) {
        String url = String.format(ExternalUrl.GR_CODE_URL, accessToken);
        WcQrCodeParam param = new WcQrCodeParam();
        param.setPage(ExternalUrl.ROOM_PAGE);
        param.setScene(scene);
        String paramContent = gson.toJson(param, WcQrCodeParam.class);
        return HttpClient.postJson(url, paramContent);
    }

    public WsResult sendTemplateMessage(String openId, String formId, String time, String amount, String projectName, String queryString) {
        String accessToken = getAccessToken();
        String url = String.format(ExternalUrl.TEMPLATE_MESSAGE_URL, accessToken);
        WcTemplateMessageParam param = new WcTemplateMessageParam();
        param.setTemplate_id(wcConstants.getBookId());
        param.setTouser(openId);
        param.setForm_id(formId);
        param.setPage(ExternalUrl.TECHNICIAN_MESSAGE_PAGE);
        WcTemplateData data = new WcTemplateData();
        data.setKeyword1(time);
        data.setKeyword2(amount);
        data.setKeyword3(projectName);
        String paramContent = gson.toJson(param, WcTemplateMessageParam.class);
        String result = HttpClient.postJsonStr(url, paramContent);
        return gson.fromJson(result, WsResult.class);
    }
}
