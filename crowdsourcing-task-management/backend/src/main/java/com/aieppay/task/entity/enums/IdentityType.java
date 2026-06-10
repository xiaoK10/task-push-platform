package com.aieppay.task.entity.enums;

public enum IdentityType {
    WORKER(1, "执行者"),
    EMPLOYER(2, "雇主"),
    ADMIN(3, "管理员");

    private final Integer code;
    private final String desc;

    IdentityType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static IdentityType fromCode(Integer code) {
        for (IdentityType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return WORKER;
    }
}