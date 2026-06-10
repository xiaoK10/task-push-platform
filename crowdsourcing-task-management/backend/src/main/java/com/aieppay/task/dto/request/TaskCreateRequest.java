package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TaskCreateRequest {

    @NotBlank(message = "任务标题不能为空")
    private String title;

    @NotNull(message = "任务分类不能为空")
    private Integer category;

    private String description;

    private String steps;

    private List<String> exampleImages;

    @NotNull(message = "截止时间不能为空")
    private LocalDateTime deadline;

    @NotNull(message = "单任务佣金不能为空")
    private BigDecimal commission;

    @NotNull(message = "可接人数不能为空")
    private Integer maxWorkers;

    @NotNull(message = "总预算不能为空")
    private BigDecimal totalBudget;

    private String acceptanceCriteria;

    @NotNull(message = "交付类型不能为空")
    private Integer deliveryType;

    private String rejectRules;

    private String permissionSettings;

    private Integer autoAuditHours;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCategory() { return category; }
    public void setCategory(Integer category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public List<String> getExampleImages() { return exampleImages; }
    public void setExampleImages(List<String> exampleImages) { this.exampleImages = exampleImages; }

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

    public Integer getAutoAuditHours() { return autoAuditHours; }
    public void setAutoAuditHours(Integer autoAuditHours) { this.autoAuditHours = autoAuditHours; }

    public String getPermissionSettings() { return permissionSettings; }
    public void setPermissionSettings(String permissionSettings) { this.permissionSettings = permissionSettings; }

}
