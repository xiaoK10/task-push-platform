package com.aieppay.task.dto.response;


public class UserInfoResponse {

    private Long id;
    private String nickName;
    private String avatar;
    private Integer identityType;
    private Integer authStatus;
    private Integer creditScore;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Integer getIdentityType() { return identityType; }
    public void setIdentityType(Integer identityType) { this.identityType = identityType; }

    public Integer getAuthStatus() { return authStatus; }
    public void setAuthStatus(Integer authStatus) { this.authStatus = authStatus; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

}
