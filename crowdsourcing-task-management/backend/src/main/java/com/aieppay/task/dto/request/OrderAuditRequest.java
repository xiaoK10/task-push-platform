package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderAuditRequest {

    @NotNull(message = "订单ID不能为空")
    private List<Long> orderIds;

    @NotNull(message = "审核结果不能为空")
    private Integer result;

    private String rejectReason;

    public List<Long> getOrderIds() { return orderIds; }
    public void setOrderIds(List<Long> orderIds) { this.orderIds = orderIds; }

    public Integer getResult() { return result; }
    public void setResult(Integer result) { this.result = result; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

}
