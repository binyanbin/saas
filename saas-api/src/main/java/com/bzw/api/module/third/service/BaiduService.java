package com.bzw.api.module.third.service;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.bzw.api.module.main.biz.*;
import com.bzw.api.module.main.enums.RoleType;
import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.model.Tenant;
import com.bzw.api.module.base.model.User;
import com.bzw.api.module.third.constants.BaiduConstants;
import com.bzw.common.cache.CacheKeyPrefix;
import com.bzw.common.cache.ICacheClient;
import com.bzw.common.content.WebSession;
import com.bzw.common.content.WebSessionManager;
import com.bzw.common.exception.api.PhoneNotExisitsException;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.exception.api.WechatLoginFailException;
import com.bzw.common.exception.api.WrongSmsCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Service
public class BaiduService {

    @Autowired
    private BaiduConstants baiduConstants;

    public byte[] getVoice(String voice) {
        AipSpeech client = new AipSpeech(baiduConstants.getAppId(), baiduConstants.getAppKey(), baiduConstants.getSecret());
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        TtsResponse res = client.synthesis(voice, "zh", 1, null);
        byte[] data = res.getData();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "output.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;

    }


}
