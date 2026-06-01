package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinanceSummaryVO {

    /**
     * 总任务数
     */
    private long totalTaskCount;

    /**
     * 总订单数
     */
    private long totalOrderCount;

    /**
     * 通过订单数
     */
    private long approvedOrderCount;

    /**
     * 总佣金支出
     */
    private BigDecimal totalCommission;

    /**
     * 总分润支出
     */
    private BigDecimal totalProfit;

    /**
     * 总提现金额
     */
    private BigDecimal totalWithdraw;

    /**
     * 总手续费
     */
    private BigDecimal totalWithdrawFee;

    /**
     * 总用户数
     */
    private long totalUserCount;

    /**
     * 用户总余额
     */
    private BigDecimal totalBalance;
}
