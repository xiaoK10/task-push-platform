package com.aieppay.task.service.impl;

import com.aieppay.task.dto.request.WithdrawRequest;
import com.aieppay.task.dto.response.BalanceResponse;
import com.aieppay.task.entity.FundFlow;
import com.aieppay.task.entity.UserBalance;
import com.aieppay.task.entity.UserInfo;
import com.aieppay.task.entity.enums.FundFlowType;
import com.aieppay.task.exception.ServiceException;
import com.aieppay.task.mapper.FundFlowMapper;
import com.aieppay.task.mapper.UserBalanceMapper;
import com.aieppay.task.mapper.UserInfoMapper;
import com.aieppay.task.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final UserBalanceMapper userBalanceMapper;
    private final FundFlowMapper fundFlowMapper;
    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void recharge(Long userId, BigDecimal amount) {
        UserBalance balance = userBalanceMapper.selectByUserId(userId);
        if (balance == null) {
            balance = new UserBalance();
            balance.setUserId(userId);
            balance.setAvailableBalance(BigDecimal.ZERO);
            balance.setFrozenBalance(BigDecimal.ZERO);
            balance.setTotalRecharge(BigDecimal.ZERO);
            balance.setTotalWithdraw(BigDecimal.ZERO);
            userBalanceMapper.insert(balance);
        }

        BigDecimal before = balance.getAvailableBalance();
        balance.setAvailableBalance(before.add(amount));
        balance.setTotalRecharge(balance.getTotalRecharge().add(amount));
        userBalanceMapper.updateById(balance);

        recordFundFlow(userId, FundFlowType.RECHARGE.getCode(), amount, before, balance.getAvailableBalance(), null, null, "充值");
    }

    @Override
    @Transactional
    public void freezeCommission(Long employerId, BigDecimal amount, Long taskId) {
        UserBalance balance = userBalanceMapper.selectByUserId(employerId);
        if (balance == null || balance.getAvailableBalance().compareTo(amount) < 0) {
            throw ServiceException.of("余额不足");
        }

        BigDecimal before = balance.getAvailableBalance();
        balance.setAvailableBalance(before.subtract(amount));
        balance.setFrozenBalance(balance.getFrozenBalance().add(amount));
        userBalanceMapper.updateById(balance);

        recordFundFlow(employerId, FundFlowType.FROZEN.getCode(), amount, before, balance.getAvailableBalance(), null, taskId, "冻结佣金");
    }

    @Override
    @Transactional
    public void releaseCommission(Long employerId, BigDecimal amount, Long taskId) {
        UserBalance balance = userBalanceMapper.selectByUserId(employerId);
        if (balance == null || balance.getFrozenBalance().compareTo(amount) < 0) {
            throw ServiceException.of("冻结余额不足");
        }

        BigDecimal before = balance.getFrozenBalance();
        balance.setFrozenBalance(before.subtract(amount));
        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));
        userBalanceMapper.updateById(balance);

        recordFundFlow(employerId, FundFlowType.REFUND.getCode(), amount, before, balance.getAvailableBalance(), null, taskId, "解冻佣金");
    }

    @Override
    @Transactional
    public void payoutToWorker(Long workerId, BigDecimal amount, Long orderId) {
        UserBalance balance = userBalanceMapper.selectByUserId(workerId);
        if (balance == null) {
            balance = new UserBalance();
            balance.setUserId(workerId);
            balance.setAvailableBalance(BigDecimal.ZERO);
            balance.setFrozenBalance(BigDecimal.ZERO);
            balance.setTotalRecharge(BigDecimal.ZERO);
            balance.setTotalWithdraw(BigDecimal.ZERO);
            userBalanceMapper.insert(balance);
        }

        BigDecimal before = balance.getAvailableBalance();
        balance.setAvailableBalance(before.add(amount));
        userBalanceMapper.updateById(balance);

        recordFundFlow(workerId, FundFlowType.PAYOUT.getCode(), amount, before, balance.getAvailableBalance(), orderId, null, "佣金到账");
    }

    @Override
    @Transactional
    public void deductServiceFee(Long userId, BigDecimal amount, Long orderId) {
        UserBalance balance = userBalanceMapper.selectByUserId(userId);
        if (balance == null || balance.getAvailableBalance().compareTo(amount) < 0) {
            throw ServiceException.of("余额不足");
        }

        BigDecimal before = balance.getAvailableBalance();
        balance.setAvailableBalance(before.subtract(amount));
        userBalanceMapper.updateById(balance);

        recordFundFlow(userId, FundFlowType.SERVICE_FEE.getCode(), amount, before, balance.getAvailableBalance(), orderId, null, "扣除服务费");
    }

    @Override
    @Transactional
    public void withdraw(Long userId, WithdrawRequest request) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        if (user.getWithdrawPassword() == null || 
            !passwordEncoder.matches(request.getWithdrawPassword(), user.getWithdrawPassword())) {
            throw ServiceException.of("提现密码错误");
        }

        UserBalance balance = userBalanceMapper.selectByUserId(userId);
        if (balance == null || balance.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw ServiceException.of("余额不足");
        }

        BigDecimal before = balance.getAvailableBalance();
        balance.setAvailableBalance(before.subtract(request.getAmount()));
        balance.setTotalWithdraw(balance.getTotalWithdraw().add(request.getAmount()));
        userBalanceMapper.updateById(balance);

        recordFundFlow(userId, FundFlowType.WITHDRAW.getCode(), request.getAmount(), before, balance.getAvailableBalance(), null, null, "提现");
    }

    @Override
    public BalanceResponse getBalance(Long userId) {
        UserBalance balance = userBalanceMapper.selectByUserId(userId);
        if (balance == null) {
            BalanceResponse empty = new BalanceResponse();
            empty.setAvailableBalance(BigDecimal.ZERO);
            empty.setFrozenBalance(BigDecimal.ZERO);
            empty.setTotalRecharge(BigDecimal.ZERO);
            empty.setTotalWithdraw(BigDecimal.ZERO);
            return empty;
        }

        BalanceResponse response = new BalanceResponse();
        response.setAvailableBalance(balance.getAvailableBalance());
        response.setFrozenBalance(balance.getFrozenBalance());
        response.setTotalRecharge(balance.getTotalRecharge());
        response.setTotalWithdraw(balance.getTotalWithdraw());
        return response;
    }

    @Override
    public Map<String, Object> getFundStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        UserBalance balance = userBalanceMapper.selectByUserId(userId);
        if (balance != null) {
            stats.put("availableBalance", balance.getAvailableBalance());
            stats.put("frozenBalance", balance.getFrozenBalance());
            stats.put("totalRecharge", balance.getTotalRecharge());
            stats.put("totalWithdraw", balance.getTotalWithdraw());
        }
        return stats;
    }

    private void recordFundFlow(Long userId, Integer type, BigDecimal amount, 
                               BigDecimal balanceBefore, BigDecimal balanceAfter,
                               Long orderId, Long taskId, String remark) {
        FundFlow flow = new FundFlow();
        flow.setUserId(userId);
        flow.setType(type);
        flow.setAmount(amount);
        flow.setBalanceBefore(balanceBefore);
        flow.setBalanceAfter(balanceAfter);
        flow.setRelatedOrderId(orderId);
        flow.setRelatedTaskId(taskId);
        flow.setRemark(remark);
        flow.setStatus(1);
        flow.setCreatedAt(LocalDateTime.now());
        fundFlowMapper.insert(flow);
    }
}