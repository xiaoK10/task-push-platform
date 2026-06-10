package com.aieppay.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}