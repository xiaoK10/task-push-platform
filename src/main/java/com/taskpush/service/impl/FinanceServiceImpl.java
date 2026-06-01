package com.taskpush.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskpush.dto.FinanceQueryDTO;
import com.taskpush.entity.TaskInfo;
import com.taskpush.entity.TaskOrder;
import com.taskpush.entity.UserIncome;
import com.taskpush.entity.UserMember;
import com.taskpush.entity.UserWithdraw;
import com.taskpush.mapper.TaskInfoMapper;
import com.taskpush.mapper.TaskOrderMapper;
import com.taskpush.mapper.UserIncomeMapper;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.mapper.UserWithdrawMapper;
import com.taskpush.service.IFinanceService;
import com.taskpush.vo.FinanceSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 财务服务实现类
 */
@Slf4j
@Service
public class FinanceServiceImpl implements IFinanceService {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Autowired
    private UserIncomeMapper userIncomeMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired
    private UserWithdrawMapper userWithdrawMapper;

    @Override
    public FinanceSummaryVO getSummary(FinanceQueryDTO dto) {
        FinanceSummaryVO vo = new FinanceSummaryVO();

        // 总任务数
        Long totalTaskCount = taskInfoMapper.selectCount(new LambdaQueryWrapper<>());
        vo.setTotalTaskCount(totalTaskCount != null ? totalTaskCount : 0);

        // 总订单数
        Long totalOrderCount = taskOrderMapper.selectCount(new LambdaQueryWrapper<>());
        vo.setTotalOrderCount(totalOrderCount != null ? totalOrderCount : 0);

        // 通过订单数
        Long approvedOrderCount = taskOrderMapper.selectCount(
                new LambdaQueryWrapper<TaskOrder>().eq(TaskOrder::getAuditStatus, 1)
        );
        vo.setApprovedOrderCount(approvedOrderCount != null ? approvedOrderCount : 0);

        // 总佣金支出（从通过订单中汇总）
        List<TaskOrder> approvedOrders = taskOrderMapper.selectList(
                new LambdaQueryWrapper<TaskOrder>().eq(TaskOrder::getAuditStatus, 1)
        );
        BigDecimal totalCommission = approvedOrders.stream()
                .map(o -> o.getRealCommission() != null ? o.getRealCommission() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalCommission(totalCommission);

        // 总分润支出（从收益流水中汇总类型=2的记录）
        List<UserIncome> profitIncomes = userIncomeMapper.selectList(
                new LambdaQueryWrapper<UserIncome>().eq(UserIncome::getIncomeType, 2)
        );
        BigDecimal totalProfit = profitIncomes.stream()
                .map(i -> i.getAmount() != null ? i.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalProfit(totalProfit);

        // 总提现金额（从已打款的提现记录汇总）
        List<UserWithdraw> paidWithdraws = userWithdrawMapper.selectList(
                new LambdaQueryWrapper<UserWithdraw>().eq(UserWithdraw::getStatus, 1)
        );
        BigDecimal totalWithdraw = paidWithdraws.stream()
                .map(w -> w.getAmount() != null ? w.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalWithdraw(totalWithdraw);

        // 总手续费
        BigDecimal totalWithdrawFee = paidWithdraws.stream()
                .map(w -> w.getFee() != null ? w.getFee() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalWithdrawFee(totalWithdrawFee);

        // 总用户数
        Long totalUserCount = userMemberMapper.selectCount(new LambdaQueryWrapper<>());
        vo.setTotalUserCount(totalUserCount != null ? totalUserCount : 0);

        // 用户总余额
        List<UserMember> allMembers = userMemberMapper.selectList(new LambdaQueryWrapper<>());
        BigDecimal totalBalance = allMembers.stream()
                .map(m -> m.getBalance() != null ? m.getBalance() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalBalance(totalBalance);

        return vo;
    }

    @Override
    public void exportIncomeExcel(FinanceQueryDTO dto) {
        log.info("导出收益Excel - 骨架方法，待实现");
        // TODO: 实现Excel导出功能
    }

    @Override
    public void exportWithdrawExcel(FinanceQueryDTO dto) {
        log.info("导出提现Excel - 骨架方法，待实现");
        // TODO: 实现Excel导出功能
    }
}
