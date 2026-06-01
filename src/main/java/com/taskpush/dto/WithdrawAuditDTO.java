package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WithdrawAuditDTO {

    @NotNull(message = "提现ID不能为空")
    private Long withdrawId;

    /**
     * 审核状态: 1-打款, 2-驳回
     */
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    private String rejectReason;
}
