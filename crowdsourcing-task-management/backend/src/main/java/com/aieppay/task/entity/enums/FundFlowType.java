package com.aieppay.task.entity.enums;

public enum FundFlowType {
    RECHARGE(1, "充值"),
    FROZEN(2, "冻结"),
    PAYOUT(3, "放款"),
    SERVICE_FEE(4, "服务费"),
    WITHDRAW(5, "提现"),
    REFUND(6, "退款");

    private final Integer code;
    private final String desc;

    FundFlowType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static FundFlowType fromCode(Integer code) {
        for (FundFlowType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return RECHARGE;
    }
}