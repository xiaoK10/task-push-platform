package com.taskpush.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserMemberVO {

    private Long id;

    private String openid;

    private String phone;

    private String nickname;

    private String avatar;

    private Integer userLevel;

    private String province;

    private String city;

    private String district;

    private Long parentId;

    private String parentName;

    private BigDecimal balance;

    private BigDecimal frozenBalance;

    private BigDecimal totalIncome;

    private BigDecimal totalWithdraw;

    private Integer status;

    /**
     * 下级人数
     */
    private int teamCount;

    private LocalDateTime registerTime;
}
