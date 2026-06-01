package com.taskpush.common;

import lombok.Getter;

@Getter
public enum TaskStatusEnum {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已上架"),
    OFF_SHELF(2, "已下架"),
    FINISHED(3, "已完结");

    private final int code;
    private final String desc;

    TaskStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskStatusEnum getByCode(int code) {
        for (TaskStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
