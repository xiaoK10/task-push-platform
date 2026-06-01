package com.taskpush.dto;

import lombok.Data;

@Data
public class RiskBlackDTO {

    /**
     * 黑名单类型: 1-手机号, 2-设备ID, 3-openid
     */
    private Integer blackType;

    private String blackValue;

    private String reason;
}
