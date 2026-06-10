package com.aieppay.task.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskListResponse {

    private Long id;
    private String title;
    private Integer category;
    private String description;
    private LocalDateTime deadline;
    private BigDecimal commission;
    private Integer maxWorkers;
    private Integer remainingWorkers;
    private List<String> exampleImages;
    private Integer status;
    private LocalDateTime createdAt;
}