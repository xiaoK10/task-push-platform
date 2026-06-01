package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收益流水实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_income")
public class UserIncome {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 订单ID */
    private Long orderId;

    /** 任务ID */
    private Long taskId;

    /** 金额 */
    private BigDecimal amount;

    /** 收入类型(1任务佣金 2团队分润 3提现退回) */
    private Integer incomeType;

    /** 描述 */
    private String description;

    /** 操作后余额 */
    private BigDecimal balanceAfter;

    /** 逻辑删除(0未删除 1已删除) */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
