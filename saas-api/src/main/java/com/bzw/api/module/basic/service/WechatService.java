package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.dto.WechatAccesTokenDTO;
import com.bzw.api.module.basic.dto.WechatLoginDTO;
import com.bzw.api.module.basic.param.WechatQRCodeParam;
import com.bzw.common.cache.RedisClient;
import com.bzw.common.utils.HttpClient;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatService {

    @Autowired
    private Gson gson;

    @Autowired
    private RedisClient redisClient;

    public String getOpenId(String appid, String secret, String jscode) {
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", appid, secret, jscode);
        String body = HttpClient.get(url);
        if (StringUtils.isNotBlank(body)) {
            WechatLoginDTO result = gson.fromJson(body, WechatLoginDTO.class);
            return result.getOpenid();
        } else {
            return null;
        }
    }

    public String getAccessToken(String appid, String secret) {
        String wechatkey = "wechat_accesstoken";
        String cacheValue = redisClient.get(wechatkey);
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        } else {
            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, secret);
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
        String url = String.format("http://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s", accessToken);
        WechatQRCodeParam param = new WechatQRCodeParam();
        param.setPage(page);
        param.setScene(scene);
        String paramContent = gson.toJson(param, WechatQRCodeParam.class);
        return HttpClient.postJson(url, paramContent);
    }

}
