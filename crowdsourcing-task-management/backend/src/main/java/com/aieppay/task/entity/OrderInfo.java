package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_info")
public class OrderInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("task_id")
    private Long taskId;

    @TableField("worker_id")
    private Long workerId;

    @TableField("employer_id")
    private Long employerId;

    @TableField("commission")
    private BigDecimal commission;

    @TableField("delivery_content")
    private String deliveryContent;

    @TableField("delivery_time")
    private LocalDateTime deliveryTime;

    @TableField("status")
    private Integer status;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("audit_user_id")
    private Long auditUserId;

    @TableField("reject_reason")
    private String rejectReason;

    @TableField("retry_count")
    private Integer retryCount;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}