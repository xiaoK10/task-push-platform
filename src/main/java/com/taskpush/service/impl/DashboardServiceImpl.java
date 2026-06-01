package com.taskpush.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskpush.entity.TaskInfo;
import com.taskpush.entity.TaskOrder;
import com.taskpush.entity.UserMember;
import com.taskpush.entity.UserWithdraw;
import com.taskpush.mapper.TaskInfoMapper;
import com.taskpush.mapper.TaskOrderMapper;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.mapper.UserWithdrawMapper;
import com.taskpush.service.IDashboardService;
import com.taskpush.vo.DashboardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 仪表盘服务实现类
 */
@Slf4j
@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired
    private UserWithdrawMapper userWithdrawMapper;

    @Override
    public DashboardVO getDashboard() {
        DashboardVO vo = new DashboardVO();

        // 今日起止时间
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 今日抢单数
        Long todayGrabCount = taskOrderMapper.selectCount(
                new LambdaQueryWrapper<TaskOrder>()
                        .between(TaskOrder::getGrabTime, todayStart, todayEnd)
        );
        vo.setTodayGrabCount(todayGrabCount != null ? todayGrabCount.intValue() : 0);

        // 今日审核数
        Long todayAuditCount = taskOrderMapper.selectCount(
                new LambdaQueryWrapper<TaskOrder>()
                        .between(TaskOrder::getAuditTime, todayStart, todayEnd)
        );
        vo.setTodayAuditCount(todayAuditCount != null ? todayAuditCount.intValue() : 0);

        // 今日佣金（今日通过审核的订单佣金合计）
        java.util.List<TaskOrder> todayApprovedOrders = taskOrderMapper.selectList(
                new LambdaQueryWrapper<TaskOrder>()
                        .eq(TaskOrder::getAuditStatus, 1)
                        .between(TaskOrder::getAuditTime, todayStart, todayEnd)
        );
        BigDecimal todayCommission = todayApprovedOrders.stream()
                .map(o -> o.getRealCommission() != null ? o.getRealCommission() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTodayCommission(todayCommission);

        // 待审核订单数
        Long pendingAuditCount = taskOrderMapper.selectCount(
                new LambdaQueryWrapper<TaskOrder>().eq(TaskOrder::getAuditStatus, 0)
        );
        vo.setPendingAuditCount(pendingAuditCount != null ? pendingAuditCount.intValue() : 0);

        // 待审核提现数
        Long pendingWithdrawCount = userWithdrawMapper.selectCount(
                new LambdaQueryWrapper<UserWithdraw>().eq(UserWithdraw::getStatus, 0)
        );
        vo.setPendingWithdrawCount(pendingWithdrawCount != null ? pendingWithdrawCount.intValue() : 0);

        // 上架任务数
        Long onlineTaskCount = taskInfoMapper.selectCount(
                new LambdaQueryWrapper<TaskInfo>().eq(TaskInfo::getTaskStatus, 1)
        );
        vo.setOnlineTaskCount(onlineTaskCount != null ? onlineTaskCount.intValue() : 0);

        // 总用户数
        Long totalUserCount = userMemberMapper.selectCount(new LambdaQueryWrapper<>());
        vo.setTotalUserCount(totalUserCount != null ? totalUserCount.intValue() : 0);

        return vo;
    }
}
