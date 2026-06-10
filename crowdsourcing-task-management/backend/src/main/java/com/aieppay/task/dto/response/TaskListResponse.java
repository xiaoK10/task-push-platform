package com.aieppay.task.dto.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCategory() { return category; }
    public void setCategory(Integer category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }

    public Integer getMaxWorkers() { return maxWorkers; }
    public void setMaxWorkers(Integer maxWorkers) { this.maxWorkers = maxWorkers; }

    public Integer getRemainingWorkers() { return remainingWorkers; }
    public void setRemainingWorkers(Integer remainingWorkers) { this.remainingWorkers = remainingWorkers; }

    public List<String> getExampleImages() { return exampleImages; }
    public void setExampleImages(List<String> exampleImages) { this.exampleImages = exampleImages; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
