package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCategory() { return category; }
    public void setCategory(Integer category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public String getExampleImages() { return exampleImages; }
    public void setExampleImages(String exampleImages) { this.exampleImages = exampleImages; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }

    public Integer getMaxWorkers() { return maxWorkers; }
    public void setMaxWorkers(Integer maxWorkers) { this.maxWorkers = maxWorkers; }

    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }

    public String getAcceptanceCriteria() { return acceptanceCriteria; }
    public void setAcceptanceCriteria(String acceptanceCriteria) { this.acceptanceCriteria = acceptanceCriteria; }

    public Integer getDeliveryType() { return deliveryType; }
    public void setDeliveryType(Integer deliveryType) { this.deliveryType = deliveryType; }

    public String getRejectRules() { return rejectRules; }
    public void setRejectRules(String rejectRules) { this.rejectRules = rejectRules; }

    public String getPermissionSettings() { return permissionSettings; }
    public void setPermissionSettings(String permissionSettings) { this.permissionSettings = permissionSettings; }

    public Long getEmployerId() { return employerId; }
    public void setEmployerId(Long employerId) { this.employerId = employerId; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public Integer getAutoAuditHours() { return autoAuditHours; }
    public void setAutoAuditHours(Integer autoAuditHours) { this.autoAuditHours = autoAuditHours; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
