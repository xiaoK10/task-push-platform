package com.aieppay.task.dto.response;

import lombok.Data;

@Data
public class UserInfoResponse {

    private Long id;
    private String nickName;
    private String avatar;
    private Integer identityType;
    private Integer authStatus;
    private Integer creditScore;
    private Integer status;
}