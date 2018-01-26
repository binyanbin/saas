package com.bzw.api.module.basic.service;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.bzw.api.module.basic.constant.BaiduConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
