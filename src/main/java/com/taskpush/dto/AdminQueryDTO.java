package com.taskpush.dto;

import lombok.Data;

@Data
public class AdminQueryDTO {

    private String username;

    private String realName;

    private Integer status;

    private int pageNum = 1;

    private int pageSize = 10;
}
