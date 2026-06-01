package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WithdrawApplyDTO {

    @NotNull(message = "提现金额不能为空")
    private BigDecimal amount;

    /**
     * 提现类型: 1-微信, 2-银行卡
     */
    @NotNull(message = "提现类型不能为空")
    private Integer withdrawType;

    private String receiveAccount;

    private String receiveName;

    private String bankName;

    private String bankBranch;
}
