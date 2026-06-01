package com.taskpush.dto;

import lombok.Data;

@Data
public class TaskQueryDTO {

    private Long categoryId;

    private String taskName;

    private Integer taskStatus;

    private Integer pageNum;

    private Integer pageSize;

    private String startTime;

    private String endTime;
}
