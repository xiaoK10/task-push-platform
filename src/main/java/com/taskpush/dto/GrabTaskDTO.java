package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GrabTaskDTO {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;
}
