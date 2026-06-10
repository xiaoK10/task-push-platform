package com.aieppay.task.entity.enums;

public enum TaskStatus {
    DRAFT(0, "草稿"),
    PENDING_AUDIT(1, "待审核"),
    ONLINE(2, "已上架"),
    OFFLINE(3, "已下架"),
    EXPIRED(4, "已过期");

    private final Integer code;
    private final String desc;

    TaskStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TaskStatus fromCode(Integer code) {
        for (TaskStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return DRAFT;
    }
}