package com.bzw.common.exception;

/**
 * @author :yanbin
 */
public enum ApplicationErrorCode {
    /*
      接口异常
     */
    ArgumentsIncorrect("10001", "参数不正确"),
    InvalidAccessToken("10010", "会话已失效,请重新登录"),
    UnKnowException("10014", "发生未知异常"),
    NoHandlerFound("10015","无效的请求路径"),
    UserLoginFail("10016","用户名或密码错误,登陆失败，请重新登陆"),
    OpenIdLoginFail("10017","openid未绑定,登陆失败，"),
    DuplicateSubmit("10022", "请勿重复提交数据"),
    IOException("20005", "IO异常"),
    InvalidSign("20006", "无效的签名"),
    UsedSignature("20007", "签名已使用"),
    RedisLockException("20008","获取锁异常"),
    EventHandlerTimeout("20009","事件处理超时"),
    ;

    private String code;
    private String reasoning;

    ApplicationErrorCode(String code, String reasoning) {
        this.code = code;
        this.reasoning = reasoning;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the reasoning
     */
    public String getReasoning() {
        return reasoning;
    }


}
