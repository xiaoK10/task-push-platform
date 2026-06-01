package com.taskpush.dto;

import lombok.Data;

@Data
public class UserQueryDTO {

    private String phone;

    private String nickname;

    private Integer userLevel;

    private Integer status;

    private Long parentId;

    private Integer pageNum;

    private Integer pageSize;
}
