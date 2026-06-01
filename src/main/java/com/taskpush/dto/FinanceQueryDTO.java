package com.taskpush.dto;

import lombok.Data;

@Data
public class FinanceQueryDTO {

    private String startTime;

    private String endTime;

    private Integer pageNum;

    private Integer pageSize;
}
