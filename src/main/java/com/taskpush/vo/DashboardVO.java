package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardVO {

    private Integer todayGrabCount;

    private Integer todayAuditCount;

    private BigDecimal todayCommission;

    private Integer pendingAuditCount;

    private Integer pendingWithdrawCount;

    private Integer onlineTaskCount;

    private Integer totalUserCount;
}
