package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 团队分润记录实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_team_profit")
public class UserTeamProfit {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 受益上级用户ID */
    private Long userId;

    /** 来源下级用户ID */
    private Long fromUserId;

    /** 订单ID */
    private Long orderId;

    /** 任务ID */
    private Long taskId;

    /** 分润金额 */
    private BigDecimal profitAmount;

    /** 分润比例(%) */
    private BigDecimal profitRatio;

    /** 分润层级 */
    private Integer profitLevel;

    /** 逻辑删除(0未删除 1已删除) */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
