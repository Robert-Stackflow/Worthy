package com.cloudchewie.client.http;

public enum ResponseCode {
    RC100(100, "操作成功"),
    RC101(101, "注册成功"),
    RC102(102, "登陆成功"),
    RC103(103, "退出登录"),
    RC104(104, "注销成功"),
    RC105(105, "更新成功"),
    RC106(106, "手机号码更改成功"),
    RC107(107, "邮箱地址更改成功"),
    RC999(999, "操作失败"),
    PASSWORD_ERROR(1002, "手机号或密码错误"),
    MOBILE_REGISTERED(1003, "手机号已注册"),
    MOBILE_NOT_REGISTERED(1004, "手机号未注册"),
    INVALID_TOKEN(2001, "访问令牌(Token)不合法"),
    ACCESS_DENIED(2003, "没有权限访问该资源");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
