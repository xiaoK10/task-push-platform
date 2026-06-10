package com.aieppay.task.entity.enums;

public enum AuthStatus {
    UN_AUTH(0, "未认证"),
    AUDITING(1, "审核中"),
    AUTHED(2, "已认证"),
    AUTH_FAILED(3, "认证失败");

    private final Integer code;
    private final String desc;

    AuthStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AuthStatus fromCode(Integer code) {
        for (AuthStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return UN_AUTH;
    }
}