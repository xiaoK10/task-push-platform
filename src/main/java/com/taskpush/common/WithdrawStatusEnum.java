package com.taskpush.common;

import lombok.Getter;

@Getter
public enum WithdrawStatusEnum {

    PENDING(0, "待审核"),
    PAID(1, "已打款"),
    REJECTED(2, "已驳回");

    private final int code;
    private final String desc;

    WithdrawStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WithdrawStatusEnum getByCode(int code) {
        for (WithdrawStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
