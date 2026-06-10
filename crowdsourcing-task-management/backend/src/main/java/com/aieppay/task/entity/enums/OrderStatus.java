package com.aieppay.task.entity.enums;

public enum OrderStatus {
    PENDING_ACCEPT(0, "待接单"),
    ACCEPTED(1, "已接单"),
    PENDING_SUBMIT(2, "待提交"),
    PENDING_AUDIT(3, "待审核"),
    AUDIT_PASS(4, "审核通过"),
    REJECTED(5, "已驳回"),
    CANCELLED(6, "已作废"),
    COMPLETED(7, "已完成");

    private final Integer code;
    private final String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatus fromCode(Integer code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return PENDING_ACCEPT;
    }
}