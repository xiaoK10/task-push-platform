package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_info")
public class UserInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("wx_open_id")
    private String wxOpenId;

    @TableField("phone")
    private String phone;

    @TableField("nick_name")
    private String nickName;

    @TableField("avatar")
    private String avatar;

    @TableField("identity_type")
    private Integer identityType;

    @TableField("real_name")
    private String realName;

    @TableField("id_card")
    private String idCard;

    @TableField("enterprise_name")
    private String enterpriseName;

    @TableField("enterprise_license")
    private String enterpriseLicense;

    @TableField("auth_status")
    private Integer authStatus;

    @TableField("credit_score")
    private Integer creditScore;

    @TableField("bank_card")
    private String bankCard;

    @TableField("bank_name")
    private String bankName;

    @TableField("alipay_account")
    private String alipayAccount;

    @TableField("withdraw_password")
    private String withdrawPassword;

    @TableField("status")
    private Integer status;

    @TableField("blacklisted")
    private Integer blacklisted;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}