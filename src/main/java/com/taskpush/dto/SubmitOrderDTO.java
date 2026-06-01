package com.taskpush.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SubmitOrderDTO {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 凭证图片(JSON数组字符串)
     */
    @NotEmpty(message = "凭证图片不能为空")
    private String certImages;
}
