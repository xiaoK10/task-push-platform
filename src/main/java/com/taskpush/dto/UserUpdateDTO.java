package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private Integer userLevel;

    /**
     * 状态: 0-拉黑, 1-正常, 2-冻结
     */
    private Integer status;

    private String remark;
}
