package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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

    private PermissionSettings permissionSettings;

    private Integer autoAuditHours;

    @Data
    public static class PermissionSettings {
        private Boolean allowNewWorker;
        private Boolean allowHighCredit;
        private Boolean restrictRepeat;
        private List<Long> assignedUserIds;
    }
}