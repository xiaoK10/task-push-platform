package com.taskpush.common;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {

    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回"),
    SUPPLEMENT(3, "补资料"),
    CANCELLED(4, "已取消");

    private final int code;
    private final String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderStatusEnum getByCode(int code) {
        for (OrderStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
