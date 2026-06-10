package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWxOpenId() { return wxOpenId; }
    public void setWxOpenId(String wxOpenId) { this.wxOpenId = wxOpenId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Integer getIdentityType() { return identityType; }
    public void setIdentityType(Integer identityType) { this.identityType = identityType; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getEnterpriseName() { return enterpriseName; }
    public void setEnterpriseName(String enterpriseName) { this.enterpriseName = enterpriseName; }

    public String getEnterpriseLicense() { return enterpriseLicense; }
    public void setEnterpriseLicense(String enterpriseLicense) { this.enterpriseLicense = enterpriseLicense; }

    public Integer getAuthStatus() { return authStatus; }
    public void setAuthStatus(Integer authStatus) { this.authStatus = authStatus; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public String getBankCard() { return bankCard; }
    public void setBankCard(String bankCard) { this.bankCard = bankCard; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAlipayAccount() { return alipayAccount; }
    public void setAlipayAccount(String alipayAccount) { this.alipayAccount = alipayAccount; }

    public String getWithdrawPassword() { return withdrawPassword; }
    public void setWithdrawPassword(String withdrawPassword) { this.withdrawPassword = withdrawPassword; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getBlacklisted() { return blacklisted; }
    public void setBlacklisted(Integer blacklisted) { this.blacklisted = blacklisted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
