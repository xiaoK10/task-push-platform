package com.taskpush.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminVO {

    private Long id;

    private String username;

    private String realName;

    private String phone;

    private Long roleId;

    private String roleName;

    private Integer status;

    private LocalDateTime createTime;
}
