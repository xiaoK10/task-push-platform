package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    @NotNull(message = "提现金额不能为空")
    private BigDecimal amount;

    @NotBlank(message = "提现密码不能为空")
    private String withdrawPassword;

    private Integer withdrawType;
}