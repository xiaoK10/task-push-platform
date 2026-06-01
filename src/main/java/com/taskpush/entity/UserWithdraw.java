package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现申请实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_withdraw")
public class UserWithdraw {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 提现单号 */
    private String withdrawNo;

    /** 用户ID */
    private Long userId;

    /** 提现金额 */
    private BigDecimal amount;

    /** 手续费 */
    private BigDecimal fee;

    /** 实际到账金额 */
    private BigDecimal realAmount;

    /** 提现方式(1微信 2银行卡) */
    private Integer withdrawType;

    /** 收款账号 */
    private String receiveAccount;

    /** 收款人姓名 */
    private String receiveName;

    /** 银行名称 */
    private String bankName;

    /** 支行信息 */
    private String bankBranch;

    /** 状态(0待审 1已打款 2驳回) */
    private Integer status;

    /** 审核人ID */
    private Long auditId;

    /** 审核时间 */
    private LocalDateTime auditTime;

    /** 打款时间 */
    private LocalDateTime payTime;

    /** 驳回原因 */
    private String rejectReason;

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
