package com.taskpush.vo;

import lombok.Data;

import java.util.List;

@Data
public class LoginVO {

    private String token;

    private Long adminId;

    private String username;

    private String realName;

    private String roleName;

    private List<MenuVO> menus;
}
