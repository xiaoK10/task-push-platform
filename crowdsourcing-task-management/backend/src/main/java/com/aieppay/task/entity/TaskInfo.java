package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("task_info")
public class TaskInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("category")
    private Integer category;

    @TableField("description")
    private String description;

    @TableField("steps")
    private String steps;

    @TableField("example_images")
    private String exampleImages;

    @TableField("deadline")
    private LocalDateTime deadline;

    @TableField("commission")
    private BigDecimal commission;

    @TableField("max_workers")
    private Integer maxWorkers;

    @TableField("total_budget")
    private BigDecimal totalBudget;

    @TableField("acceptance_criteria")
    private String acceptanceCriteria;

    @TableField("delivery_type")
    private Integer deliveryType;

    @TableField("reject_rules")
    private String rejectRules;

    @TableField("permission_settings")
    private String permissionSettings;

    @TableField("employer_id")
    private Long employerId;

    @TableField("status")
    private Integer status;

    @TableField("audit_status")
    private Integer auditStatus;

    @TableField("reject_reason")
    private String rejectReason;

    @TableField("auto_audit_hours")
    private Integer autoAuditHours;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}