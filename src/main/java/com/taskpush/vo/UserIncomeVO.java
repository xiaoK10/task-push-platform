package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserIncomeVO {

    private Long id;

    private Long userId;

    private Long orderId;

    private String taskName;

    private BigDecimal amount;

    private Integer incomeType;

    private String incomeTypeName;

    private String description;

    private BigDecimal balanceAfter;

    private LocalDateTime createTime;
}
