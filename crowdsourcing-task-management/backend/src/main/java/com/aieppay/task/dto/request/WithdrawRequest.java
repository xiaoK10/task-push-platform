package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WithdrawRequest {

    @NotNull(message = "提现金额不能为空")
    private BigDecimal amount;

    @NotBlank(message = "提现密码不能为空")
    private String withdrawPassword;

    private Integer withdrawType;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getWithdrawPassword() { return withdrawPassword; }
    public void setWithdrawPassword(String withdrawPassword) { this.withdrawPassword = withdrawPassword; }

    public Integer getWithdrawType() { return withdrawType; }
    public void setWithdrawType(Integer withdrawType) { this.withdrawType = withdrawType; }

}
