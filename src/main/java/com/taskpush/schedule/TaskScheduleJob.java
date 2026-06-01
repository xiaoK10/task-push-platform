package com.taskpush.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.taskpush.entity.TaskInfo;
import com.taskpush.entity.TaskOrder;
import com.taskpush.entity.UserIncome;
import com.taskpush.entity.UserMember;
import com.taskpush.mapper.TaskInfoMapper;
import com.taskpush.mapper.TaskOrderMapper;
import com.taskpush.mapper.UserIncomeMapper;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.mapper.UserTeamProfitMapper;
import com.taskpush.service.ITaskInfoService;
import com.taskpush.service.ITaskOrderService;
import com.taskpush.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * XXL-Job 定时任务处理器
 *
 * @author task-push
 */
@Slf4j
@Component
public class TaskScheduleJob {

    @Autowired
    private ITaskOrderService taskOrderService;

    @Autowired
    private ITaskInfoService taskInfoService;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Autowired
    private UserIncomeMapper userIncomeMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired(required = false)
    private UserTeamProfitMapper userTeamProfitMapper;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 释放过期订单
     * 查询所有 auditStatus=0(待审核) 且 expireTime < now() 的订单，逐条取消释放库存
     * Cron: 每5分钟执行一次
     */
    @XxlJob("releaseExpiredOrders")
    public void releaseExpiredOrders() {
        log.info("========== 开始执行：释放过期订单 ==========");
        try {
            LocalDateTime now = LocalDateTime.now();
            LambdaQueryWrapper<TaskOrder> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TaskOrder::getAuditStatus, 0)
                   .lt(TaskOrder::getExpireTime, now);

            List<TaskOrder> expiredOrders = taskOrderMapper.selectList(wrapper);
            if (expiredOrders == null || expiredOrders.isEmpty()) {
                XxlJobHelper.log("未发现过期订单");
                XxlJobHelper.handleSuccess();
                return;
            }

            int releasedCount = 0;
            for (TaskOrder order : expiredOrders) {
                try {
                    taskOrderService.cancelOrder(order.getId());
                    releasedCount++;
                    log.debug("释放过期订单: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
                } catch (Exception e) {
                    log.error("释放过期订单失败: orderId={}, error={}", order.getId(), e.getMessage());
                    XxlJobHelper.log("释放失败 - 订单ID: " + order.getId() + ", 原因: " + e.getMessage());
                }
            }

            XxlJobHelper.log("本次共释放过期订单: " + releasedCount + " 条");
            log.info("释放过期订单完成，共释放 {} 条", releasedCount);
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("释放过期订单任务异常", e);
            XxlJobHelper.log("任务异常: " + e.getMessage());
            XxlJobHelper.handleFail();
        }
    }

    /**
     * 每日结算报表
     * 统计昨日：订单总数、通过数、驳回数、佣金总支出、分润总支出、新注册用户数
     * Cron: 每天凌晨2点执行
     */
    @XxlJob("dailySettlementReport")
    public void dailySettlementReport() {
        log.info("========== 开始执行：每日结算报表 ==========");
        try {
            // 计算昨日时间范围
            LocalDate yesterday = LocalDate.now().minusDays(1);
            String startDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
            String endDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
            String dateStr = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 1. 昨日订单总数（按 createTime 在昨日范围内的 task_order 记录）
            LambdaQueryWrapper<TaskOrder> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.ge(TaskOrder::getCreateTime, startDate)
                        .le(TaskOrder::getCreateTime, endDate);
            long totalOrders = taskOrderMapper.selectCount(orderWrapper);

            // 2. 通过数 (auditStatus = 1)
            LambdaQueryWrapper<TaskOrder> passWrapper = new LambdaQueryWrapper<>();
            passWrapper.eq(TaskOrder::getAuditStatus, 1)
                       .ge(TaskOrder::getCreateTime, startDate)
                       .le(TaskOrder::getCreateTime, endDate);
            long passOrders = taskOrderMapper.selectCount(passWrapper);

            // 3. 驳回数 (auditStatus = 2)
            LambdaQueryWrapper<TaskOrder> rejectWrapper = new LambdaQueryWrapper<>();
            rejectWrapper.eq(TaskOrder::getAuditStatus, 2)
                         .ge(TaskOrder::getCreateTime, startDate)
                         .le(TaskOrder::getCreateTime, endDate);
            long rejectOrders = taskOrderMapper.selectCount(rejectWrapper);

            // 4. 佣金总支出（user_income 中 incomeType=1 的 amount 汇总）
            LambdaQueryWrapper<UserIncome> commissionWrapper = new LambdaQueryWrapper<>();
            commissionWrapper.eq(UserIncome::getIncomeType, 1)
                             .ge(UserIncome::getCreateTime, startDate)
                             .le(UserIncome::getCreateTime, endDate);
            BigDecimal totalCommission = BigDecimal.ZERO;
            List<UserIncome> commissionList = userIncomeMapper.selectList(commissionWrapper);
            if (commissionList != null) {
                for (UserIncome income : commissionList) {
                    if (income.getAmount() != null) {
                        totalCommission = totalCommission.add(income.getAmount());
                    }
                }
            }

            // 5. 分润总支出（user_income 中 incomeType=2 的 amount 汇总）
            LambdaQueryWrapper<UserIncome> profitWrapper = new LambdaQueryWrapper<>();
            profitWrapper.eq(UserIncome::getIncomeType, 2)
                         .ge(UserIncome::getCreateTime, startDate)
                         .le(UserIncome::getCreateTime, endDate);
            BigDecimal totalProfit = BigDecimal.ZERO;
            List<UserIncome> profitList = userIncomeMapper.selectList(profitWrapper);
            if (profitList != null) {
                for (UserIncome income : profitList) {
                    if (income.getAmount() != null) {
                        totalProfit = totalProfit.add(income.getAmount());
                    }
                }
            }

            // 6. 新注册用户数（user_member 中 registerTime 在昨日范围内的记录）
            LambdaQueryWrapper<UserMember> memberWrapper = new LambdaQueryWrapper<>();
            memberWrapper.ge(UserMember::getRegisterTime, startDate)
                         .le(UserMember::getRegisterTime, endDate);
            long newUsers = userMemberMapper.selectCount(memberWrapper);

            // 输出汇总日志
            StringBuilder report = new StringBuilder();
            report.append("\n========================================\n");
            report.append("        每日结算报表 - ").append(dateStr).append("\n");
            report.append("========================================\n");
            report.append("订单总数:       ").append(totalOrders).append(" 笔\n");
            report.append("审核通过:       ").append(passOrders).append(" 笔\n");
            report.append("审核驳回:       ").append(rejectOrders).append(" 笔\n");
            report.append("佣金总支出:     ").append(totalCommission).append(" 元\n");
            report.append("分润总支出:     ").append(totalProfit).append(" 元\n");
            report.append("新注册用户:     ").append(newUsers).append(" 人\n");
            report.append("========================================\n");

            XxlJobHelper.log(report.toString());
            log.info("每日结算报表生成完成: {}", dateStr);

            // TODO: 预留写入 sys_daily_report 统计表

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("每日结算报表任务异常", e);
            XxlJobHelper.log("任务异常: " + e.getMessage());
            XxlJobHelper.handleFail();
        }
    }

    /**
     * 自动下架过期任务
     * 查询 taskStatus=1(上架) 且 taskEndTime < now() 的任务，批量更新为 3(已完结)
     * Cron: 每小时执行一次
     */
    @XxlJob("autoOfflineExpiredTasks")
    public void autoOfflineExpiredTasks() {
        log.info("========== 开始执行：自动下架过期任务 ==========");
        try {
            LocalDateTime now = LocalDateTime.now();
            LambdaQueryWrapper<TaskInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TaskInfo::getTaskStatus, 1)
                   .lt(TaskInfo::getTaskEndTime, now);

            List<TaskInfo> expiredTasks = taskInfoMapper.selectList(wrapper);
            if (expiredTasks == null || expiredTasks.isEmpty()) {
                XxlJobHelper.log("未发现需自动下架的过期任务");
                XxlJobHelper.handleSuccess();
                return;
            }

            int offlineCount = 0;
            for (TaskInfo task : expiredTasks) {
                try {
                    taskInfoService.updateStatus(task.getId(), 3);
                    offlineCount++;
                    log.debug("自动下架过期任务: taskId={}, taskName={}", task.getId(), task.getTaskName());
                } catch (Exception e) {
                    log.error("自动下架任务失败: taskId={}, error={}", task.getId(), e.getMessage());
                    XxlJobHelper.log("下架失败 - 任务ID: " + task.getId() + ", 原因: " + e.getMessage());
                }
            }

            XxlJobHelper.log("本次共自动下架过期任务: " + offlineCount + " 个");
            log.info("自动下架过期任务完成，共下架 {} 个", offlineCount);
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("自动下架过期任务异常", e);
            XxlJobHelper.log("任务异常: " + e.getMessage());
            XxlJobHelper.handleFail();
        }
    }

    /**
     * 清理过期Token缓存
     * Redis scan USER_TOKEN_PREFIX 和 ADMIN_TOKEN_PREFIX 相关的过期key并清理
     * Cron: 每天凌晨3点执行
     */
    @XxlJob("clearExpiredTokenCache")
    public void clearExpiredTokenCache() {
        log.info("========== 开始执行：清理过期Token缓存 ==========");
        try {
            if (redisTemplate == null) {
                XxlJobHelper.log("RedisTemplate 未注入，跳过Token缓存清理");
                XxlJobHelper.handleSuccess();
                return;
            }

            int deletedCount = 0;

            // 清理 USER_TOKEN_PREFIX 相关key
            deletedCount += scanAndDeletePrefix(CommonConstant.USER_TOKEN_PREFIX);

            // 清理 ADMIN_TOKEN_PREFIX 相关key
            deletedCount += scanAndDeletePrefix(CommonConstant.ADMIN_TOKEN_PREFIX);

            XxlJobHelper.log("本次共清理过期Token缓存: " + deletedCount + " 条");
            log.info("清理过期Token缓存完成，共清理 {} 条", deletedCount);
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("清理过期Token缓存异常", e);
            XxlJobHelper.log("任务异常: " + e.getMessage());
            XxlJobHelper.handleFail();
        }
    }

    /**
     * 使用 SCAN 命令扫描并统计/清理指定前缀的Key
     * 由于Redis的TTL机制会自动清理过期key，此处主要是进行scan扫描确认
     * 并记录已过期的key数量（如果key已过期则scan不到，所以主要是统计类操作）
     *
     * @param prefix key前缀
     * @return 发现的key数量
     */
    private int scanAndDeletePrefix(String prefix) {
        int count = 0;
        try {
            ScanOptions options = ScanOptions.scanOptions()
                    .match(prefix + "*")
                    .count(100)
                    .build();

            // 使用 StringRedisTemplate 进行 scan 操作（更简单可靠）
            org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate =
                    new org.springframework.data.redis.core.StringRedisTemplate(
                            redisTemplate.getConnectionFactory());

            Cursor<String> cursor = stringRedisTemplate.scan(options);
            while (cursor.hasNext()) {
                String key = cursor.next();
                count++;
                // 检查key是否存在（已过期的key不会出现在scan结果中）
                // 对仍然存在的key，检查TTL，如果即将过期则主动删除
                Long ttl = redisTemplate.getExpire(key);
                if (ttl != null && ttl <= 60) {
                    redisTemplate.delete(key);
                    log.debug("主动清理即将过期token: {}", key);
                }
            }
            cursor.close();
        } catch (Exception e) {
            log.warn("扫描前缀 {} 的Key时发生异常: {}", prefix, e.getMessage());
        }
        return count;
    }
}
