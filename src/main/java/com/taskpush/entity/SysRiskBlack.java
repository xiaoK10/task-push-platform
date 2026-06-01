package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 风控黑名单实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_risk_black")
public class SysRiskBlack {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 黑名单类型(1手机号 2设备ID 3OpenID) */
    private Integer blackType;

    /** 黑名单值 */
    private String blackValue;

    /** 拉黑原因 */
    private String reason;

    /** 状态(0禁用 1启用) */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
