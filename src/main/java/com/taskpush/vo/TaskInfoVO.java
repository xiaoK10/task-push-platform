package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TaskInfoVO {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String taskName;

    private String taskDesc;

    private String coverImage;

    private Integer totalStock;

    private Integer remainStock;

    private Integer maxGrabPerUser;

    /**
     * 区域限制(JSON字符串)
     */
    private String regionLimit;

    private Integer levelRequirement;

    private LocalDateTime taskStartTime;

    private LocalDateTime taskEndTime;

    private Integer taskStatus;

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

    private Integer grabExpireHours;

    /**
     * 已抢数量
     */
    private int grabbedCount;

    private LocalDateTime createTime;
}
