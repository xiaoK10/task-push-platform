package com.aieppay.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RealNameAuthRequest {

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    private String enterpriseName;

    private String enterpriseLicense;

    private Integer authType;
}