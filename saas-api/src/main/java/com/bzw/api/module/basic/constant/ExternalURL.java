package com.bzw.api.module.basic.constant;

/**
 * @author yanbin
 */
public class ExternalUrl {
    public static final String ROOM_PAGE = "pages/projectlist/projectlist";
    public static final String TECHNICIAN_MESSAGE_PAGE = "pages/preview/preview";
    public static final String OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public static final String GR_CODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    public static final String TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s";
    public static final String WE_CHAT_KEY = "wechat_accesstoken";
    public static final String SMS_URL = "http://smsapi.c123.cn/OpenPlatform/OpenApi?action=sendOnce&ac=%s&authkey=%s&cgid=%s&csid=&c=%s&m=%s";
}
