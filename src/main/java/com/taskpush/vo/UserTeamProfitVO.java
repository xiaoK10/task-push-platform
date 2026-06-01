package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserTeamProfitVO {

    private Long id;

    private Long userId;

    private String userName;

    private Long fromUserId;

    private String fromUserName;

    private Long orderId;

    private String taskName;

    private BigDecimal profitAmount;

    private BigDecimal profitRatio;

    private Integer profitLevel;

    private LocalDateTime createTime;
}
