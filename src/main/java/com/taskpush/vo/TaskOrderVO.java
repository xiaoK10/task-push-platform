package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TaskOrderVO {

    private Long id;

    private String orderNo;

    private Long userId;

    private String userNickname;

    private String userPhone;

    private Long taskId;

    private String taskName;

    private LocalDateTime grabTime;

    private LocalDateTime submitTime;

    /**
     * 凭证图片(JSON字符串)
     */
    private String certImages;

    private Integer auditStatus;

    private BigDecimal realCommission;

    private String rejectReason;

    private LocalDateTime expireTime;

    private LocalDateTime createTime;
}
