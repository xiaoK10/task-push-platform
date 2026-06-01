package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 任务主表实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "task_info", autoResultMap = true)
public class TaskInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务分类ID */
    private Long categoryId;

    /** 任务名称 */
    private String taskName;

    /** 任务详情(富文本) */
    private String taskDesc;

    /** 封面图 */
    private String coverImage;

    /** 总库存 */
    private Integer totalStock;

    /** 剩余库存 */
    private Integer remainStock;

    /** 每人最大抢单数 */
    private Integer maxGrabPerUser;

    /** 区域限制(省市区JSON) */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String regionLimit;

    /** 等级要求 */
    private Integer levelRequirement;

    /** 任务开始时间 */
    private LocalDateTime taskStartTime;

    /** 任务结束时间 */
    private LocalDateTime taskEndTime;

    /** 任务状态(0草稿 1上架 2下架 3完结) */
    private Integer taskStatus;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 阶梯佣金(JSON) */
    private String ladderCommission;

    /** 下级分润比例(%) */
    private BigDecimal profitRatio;

    /** 上传凭证配置(JSON) */
    private String certConfig;

    /** 抢单过期小时数 */
    private Integer grabExpireHours;

    /** 逻辑删除(0未删除 1已删除) */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
