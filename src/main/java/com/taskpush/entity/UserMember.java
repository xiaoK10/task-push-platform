package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 地推用户实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_member")
public class UserMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 微信OpenID */
    private String openid;

    /** 微信UnionID */
    private String unionid;

    /** 手机号 */
    private String phone;

    /** 昵称 */
    private String nickname;

    /** 头像地址 */
    private String avatar;

    /** 用户等级 */
    private Integer userLevel;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 区/县 */
    private String district;

    /** 上级代理ID */
    private Long parentId;

    /** 可用余额 */
    private BigDecimal balance;

    /** 冻结余额 */
    private BigDecimal frozenBalance;

    /** 累计收入 */
    private BigDecimal totalIncome;

    /** 累计提现 */
    private BigDecimal totalWithdraw;

    /** 状态(0黑名单 1正常 2冻结) */
    private Integer status;

    /** 逻辑删除(0未删除 1已删除) */
    @TableLogic
    private Integer isDeleted;

    /** 注册时间 */
    private LocalDateTime registerTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
