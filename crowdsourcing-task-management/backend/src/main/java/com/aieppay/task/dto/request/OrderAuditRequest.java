package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderAuditRequest {

    @NotNull(message = "订单ID不能为空")
    private List<Long> orderIds;

    @NotNull(message = "审核结果不能为空")
    private Integer result;

    private String rejectReason;
}