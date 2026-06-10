package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RealNameAuthRequest {

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    private String enterpriseName;

    private String enterpriseLicense;

    private Integer authType;

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getEnterpriseName() { return enterpriseName; }
    public void setEnterpriseName(String enterpriseName) { this.enterpriseName = enterpriseName; }

    public String getEnterpriseLicense() { return enterpriseLicense; }
    public void setEnterpriseLicense(String enterpriseLicense) { this.enterpriseLicense = enterpriseLicense; }

    public Integer getAuthType() { return authType; }
    public void setAuthType(Integer authType) { this.authType = authType; }

}
