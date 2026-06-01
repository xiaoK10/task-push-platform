package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderAuditDTO {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 审核状态: 1-通过, 2-驳回
     */
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    private String rejectReason;
}
