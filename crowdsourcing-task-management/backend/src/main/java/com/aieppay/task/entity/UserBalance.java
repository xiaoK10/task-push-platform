package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_balance")
public class UserBalance {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("available_balance")
    private BigDecimal availableBalance;

    @TableField("frozen_balance")
    private BigDecimal frozenBalance;

    @TableField("total_recharge")
    private BigDecimal totalRecharge;

    @TableField("total_withdraw")
    private BigDecimal totalWithdraw;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}