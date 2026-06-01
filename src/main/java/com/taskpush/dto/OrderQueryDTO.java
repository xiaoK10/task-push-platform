package com.taskpush.dto;

import lombok.Data;

@Data
public class OrderQueryDTO {

    private String orderNo;

    private Long userId;

    private Long taskId;

    private Integer auditStatus;

    private Integer pageNum;

    private Integer pageSize;

    private String startTime;

    private String endTime;
}
