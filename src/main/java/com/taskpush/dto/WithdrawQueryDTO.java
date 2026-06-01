package com.taskpush.dto;

import lombok.Data;

@Data
public class WithdrawQueryDTO {

    private Long userId;

    private Integer status;

    private Integer pageNum;

    private Integer pageSize;

    private String startTime;

    private String endTime;
}
