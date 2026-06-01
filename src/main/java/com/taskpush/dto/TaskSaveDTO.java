package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TaskSaveDTO {

    /**
     * 新增分组校验
     */
    public interface Create {}

    /**
     * 编辑分组校验
     */
    public interface Update {}

    @NotNull(message = "ID不能为空", groups = Update.class)
    private Long id;

    private Long categoryId;

    private String taskName;

    private String taskDesc;

    private String coverImage;

    private Integer totalStock;

    private Integer maxGrabPerUser = 3;

    /**
     * 区域限制(JSON字符串)
     */
    private String regionLimit;

    private Integer levelRequirement = 0;

    private LocalDateTime taskStartTime;

    private LocalDateTime taskEndTime;

    private BigDecimal unitPrice;

    /**
     * 阶梯佣金(JSON字符串)
     */
    private String ladderCommission;

    private BigDecimal profitRatio;

    /**
     * 凭证配置(JSON字符串)
     */
    private String certConfig;

    private Integer grabExpireHours = 24;
}
