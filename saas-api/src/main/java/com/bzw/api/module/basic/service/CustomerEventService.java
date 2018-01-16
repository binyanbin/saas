package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.OrderEventBiz;
import com.bzw.api.module.basic.biz.RoomEventBiz;
import com.bzw.api.module.basic.biz.UserEventBiz;
import com.bzw.api.module.basic.dto.WechatAccesTokenDTO;
import com.bzw.api.module.basic.dto.WechatLoginDTO;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.basic.param.WechatQRCodeParam;
import com.bzw.common.cache.RedisClient;
import com.bzw.common.content.WebSession;
import com.bzw.common.utils.HttpClient;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CustomerEventService {

    @Autowired
    private RoomEventBiz roomEventBiz;

    @Autowired
    private UserEventBiz userEventBiz;

    @Autowired
    private OrderEventBiz orderEventBiz;

    @Autowired
    private Gson gson;

    @Autowired
    private RedisClient redisClient;

    public boolean updateRoomState(Long roomId, Integer statusId, Long employeeId) {
        return roomEventBiz.updateRoomState(roomId, statusId, employeeId);
    }

    public WebSession login(String code, String password) {
        return userEventBiz.Login(code, password);
    }

    public WebSession openIdLogin(String openId) {
        return userEventBiz.openIdLogin(openId);
    }

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

    public byte[] getGrCode(String page,String accessToken,String scene) throws IOException {
        String url = String.format("http://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s",accessToken);
        WechatQRCodeParam param = new WechatQRCodeParam();
        param.setPage(page);
        param.setScene(scene);
        String paramContent = gson.toJson(param,WechatQRCodeParam.class);
        return HttpClient.postJson(url,paramContent);
    }


    public boolean bindOpenId(Long employeeId, String openId) {
        return userEventBiz.bindOpenId(employeeId, openId);
    }

    public Long addOrder(List<OrderParam> orderParams) {
        return orderEventBiz.add(orderParams);
    }

    public Long modifyOrder(Long orderId, List<OrderParam> orderParams) {
        return orderEventBiz.modify(orderId, orderParams);
    }


}
