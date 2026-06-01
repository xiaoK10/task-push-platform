package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserWithdrawVO {

    private Long id;

    private String withdrawNo;

    private Long userId;

    private String userNickname;

    private String userPhone;

    private BigDecimal amount;

    private BigDecimal fee;

    private BigDecimal realAmount;

    private Integer withdrawType;

    private String withdrawTypeName;

    private String receiveAccount;

    private String receiveName;

    private String bankName;

    private String bankBranch;

    private Integer status;

    private String statusName;

    private String rejectReason;

    private LocalDateTime auditTime;

    private LocalDateTime payTime;

    private LocalDateTime createTime;
}
