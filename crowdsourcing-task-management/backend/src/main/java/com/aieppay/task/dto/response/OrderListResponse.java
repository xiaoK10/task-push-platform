package com.aieppay.task.dto.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderListResponse {

    private Long id;
    private Long taskId;
    private String taskTitle;
    private Long workerId;
    private String workerNickName;
    private BigDecimal commission;
    private Integer status;
    private String deliveryContent;
    private LocalDateTime deliveryTime;
    private LocalDateTime auditTime;
    private String rejectReason;
    private Integer retryCount;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public String getTaskTitle() { return taskTitle; }
    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }

    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }

    public String getWorkerNickName() { return workerNickName; }
    public void setWorkerNickName(String workerNickName) { this.workerNickName = workerNickName; }

    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getDeliveryContent() { return deliveryContent; }
    public void setDeliveryContent(String deliveryContent) { this.deliveryContent = deliveryContent; }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public LocalDateTime getAuditTime() { return auditTime; }
    public void setAuditTime(LocalDateTime auditTime) { this.auditTime = auditTime; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
