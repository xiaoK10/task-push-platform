package com.aieppay.task.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
}