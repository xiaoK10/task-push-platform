package com.taskpush.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysRiskBlackVO {

    private Long id;

    private Integer blackType;

    private String blackTypeName;

    private String blackValue;

    private String reason;

    private Integer status;

    private LocalDateTime createTime;
}
