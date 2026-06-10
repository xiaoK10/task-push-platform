package com.aieppay.task.service;

import com.aieppay.task.dto.request.WithdrawRequest;
import com.aieppay.task.dto.response.BalanceResponse;

import java.math.BigDecimal;
import java.util.Map;

public interface FundService {

    void recharge(Long userId, BigDecimal amount);

    void freezeCommission(Long employerId, BigDecimal amount, Long taskId);

    void releaseCommission(Long employerId, BigDecimal amount, Long taskId);

    void payoutToWorker(Long workerId, BigDecimal amount, Long orderId);

    void deductServiceFee(Long userId, BigDecimal amount, Long orderId);

    void withdraw(Long userId, WithdrawRequest request);

    BalanceResponse getBalance(Long userId);

    Map<String, Object> getFundStatistics(Long userId);
}