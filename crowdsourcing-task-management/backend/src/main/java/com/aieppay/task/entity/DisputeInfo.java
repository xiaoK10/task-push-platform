package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("dispute_info")
public class DisputeInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("plaintiff_id")
    private Long plaintiffId;

    @TableField("defendant_id")
    private Long defendantId;

    @TableField("type")
    private Integer type;

    @TableField("content")
    private String content;

    @TableField("evidence")
    private String evidence;

    @TableField("status")
    private Integer status;

    @TableField("result")
    private Integer result;

    @TableField("result_content")
    private String resultContent;

    @TableField("arbitrator_id")
    private Long arbitratorId;

    @TableField("processed_at")
    private LocalDateTime processedAt;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getPlaintiffId() { return plaintiffId; }
    public void setPlaintiffId(Long plaintiffId) { this.plaintiffId = plaintiffId; }

    public Long getDefendantId() { return defendantId; }
    public void setDefendantId(Long defendantId) { this.defendantId = defendantId; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getEvidence() { return evidence; }
    public void setEvidence(String evidence) { this.evidence = evidence; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getResult() { return result; }
    public void setResult(Integer result) { this.result = result; }

    public String getResultContent() { return resultContent; }
    public void setResultContent(String resultContent) { this.resultContent = resultContent; }

    public Long getArbitratorId() { return arbitratorId; }
    public void setArbitratorId(Long arbitratorId) { this.arbitratorId = arbitratorId; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
