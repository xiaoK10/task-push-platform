package com.aieppay.task.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceResponse {

    private BigDecimal availableBalance;
    private BigDecimal frozenBalance;
    private BigDecimal totalRecharge;
    private BigDecimal totalWithdraw;
}