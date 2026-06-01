package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户抢单订单实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_order")
public class TaskOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 任务ID */
    private Long taskId;

    /** 抢单时间 */
    private LocalDateTime grabTime;

    /** 提交时间 */
    private LocalDateTime submitTime;

    /** 凭证图片(JSON数组) */
    private String certImages;

    /** 审核状态(0待审 1通过 2驳回 3补资料 4已取消) */
    private Integer auditStatus;

    /** 实际佣金 */
    private BigDecimal realCommission;

    /** 驳回原因 */
    private String rejectReason;

    /** 审核人ID */
    private Long auditId;

    /** 审核时间 */
    private LocalDateTime auditTime;

    /** 结算时间 */
    private LocalDateTime settleTime;

    /** 超时自动取消时间 */
    private LocalDateTime expireTime;

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
