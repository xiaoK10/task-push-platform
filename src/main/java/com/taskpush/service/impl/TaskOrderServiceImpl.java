package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taskpush.common.BusinessException;
import com.taskpush.common.PageResult;
import com.taskpush.common.ResultCode;
import com.taskpush.common.constant.CommonConstant;
import com.taskpush.dto.GrabTaskDTO;
import com.taskpush.dto.OrderAuditDTO;
import com.taskpush.dto.OrderQueryDTO;
import com.taskpush.dto.SubmitOrderDTO;
import com.taskpush.entity.TaskInfo;
import com.taskpush.entity.TaskOrder;
import com.taskpush.entity.UserIncome;
import com.taskpush.entity.UserMember;
import com.taskpush.entity.UserTeamProfit;
import com.taskpush.mapper.TaskInfoMapper;
import com.taskpush.mapper.TaskOrderMapper;
import com.taskpush.mapper.UserIncomeMapper;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.mapper.UserTeamProfitMapper;
import com.taskpush.service.ITaskInfoService;
import com.taskpush.service.ITaskOrderService;
import com.taskpush.service.ISysRiskBlackService;
import com.taskpush.vo.TaskOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 任务订单服务实现类
 */
@Slf4j
@Service
public class TaskOrderServiceImpl implements ITaskOrderService {

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired
    private UserIncomeMapper userIncomeMapper;

    @Autowired
    private UserTeamProfitMapper userTeamProfitMapper;

    @Autowired
    private ITaskInfoService taskInfoService;

    @Autowired
    private ISysRiskBlackService sysRiskBlackService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grabTask(GrabTaskDTO dto, Long userId) {
        // 1. 检查用户是否在黑名单
        UserMember member = userMemberMapper.selectById(userId);
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        if (member.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "用户已被拉黑，无法抢单");
        }
        if (member.getStatus() == 2) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "用户已被冻结，无法抢单");
        }
        // 检查手机号和openid是否在黑名单
        if (member.getPhone() != null && sysRiskBlackService.checkBlack(member.getPhone(), 1)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "该手机号已被风控拉黑");
        }
        if (member.getOpenid() != null && sysRiskBlackService.checkBlack(member.getOpenid(), 3)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "该账号已被风控拉黑");
        }

        Long taskId = dto.getTaskId();

        // 2. Redisson分布式锁锁定taskId
        RLock lock = redissonClient.getLock(CommonConstant.TASK_LOCK_PREFIX + taskId);
        try {
            boolean locked = lock.tryLock(10, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "抢单太火爆，请稍后重试");
            }

            // 3. 校验任务是否上架、剩余库存>0、未超过单人限抢次数
            TaskInfo task = taskInfoMapper.selectById(taskId);
            if (task == null) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "任务不存在");
            }
            if (task.getTaskStatus() != 1) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "任务未上架");
            }
            if (task.getRemainStock() <= 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "任务库存不足");
            }
            // 检查时间范围
            LocalDateTime now = LocalDateTime.now();
            if (task.getTaskStartTime() != null && now.isBefore(task.getTaskStartTime())) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "任务尚未开始");
            }
            if (task.getTaskEndTime() != null && now.isAfter(task.getTaskEndTime())) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "任务已结束");
            }
            // 校验单人限抢次数
            int maxGrab = task.getMaxGrabPerUser() != null ? task.getMaxGrabPerUser() : 3;
            int userGrabCount = taskOrderMapper.countUserTaskOrders(userId, taskId);
            if (userGrabCount >= maxGrab) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "已达到该任务最大抢单次数");
            }
            // 校验等级要求
            if (task.getLevelRequirement() != null && task.getLevelRequirement() > 0) {
                if (member.getUserLevel() == null || member.getUserLevel() < task.getLevelRequirement()) {
                    throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "等级不满足抢单要求");
                }
            }

            // 4. 调用 taskInfoMapper.decreaseStock 减少库存
            int affected = taskInfoMapper.decreaseStock(taskId);
            if (affected <= 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "库存扣减失败，请重试");
            }

            // 5. 生成本地订单号，创建 TaskOrder 记录
            String orderNo = generateOrderNo();
            TaskOrder order = new TaskOrder();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setTaskId(taskId);
            order.setGrabTime(now);
            order.setAuditStatus(0); // 待审核

            // 6. 设置 expireTime = now + grabExpireHours
            Integer grabExpireHours = task.getGrabExpireHours() != null ? task.getGrabExpireHours() : CommonConstant.ORDER_LOCK_TIME;
            order.setExpireTime(now.plusHours(grabExpireHours));

            taskOrderMapper.insert(order);

            log.info("用户 {} 抢单成功，订单号: {}，任务ID: {}", userId, orderNo, taskId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "抢单异常，请重试");
        } catch (BusinessException e) {
            throw e;
        } finally {
            // 7. 释放锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitCert(SubmitOrderDTO dto, Long userId) {
        TaskOrder order = taskOrderMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "订单不存在");
        }
        // 校验订单属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权操作此订单");
        }
        // 校验状态为待审核
        if (order.getAuditStatus() != 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "当前订单状态不允许提交凭证");
        }
        // 检查是否已过期
        if (order.getExpireTime() != null && LocalDateTime.now().isAfter(order.getExpireTime())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "订单已过期，无法提交凭证");
        }
        // 保存凭证图片JSON到 certImages
        order.setCertImages(dto.getCertImages());
        order.setSubmitTime(LocalDateTime.now());
        order.setAuditStatus(0); // 待审核
        taskOrderMapper.updateById(order);
    }

    @Override
    public PageResult<TaskOrderVO> pageQuery(OrderQueryDTO dto) {
        Page<TaskOrder> page = new Page<>(
                dto.getPageNum() != null ? dto.getPageNum() : 1,
                dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<TaskOrder> wrapper = new LambdaQueryWrapper<>();
        if (dto.getAuditStatus() != null) {
            wrapper.eq(TaskOrder::getAuditStatus, dto.getAuditStatus());
        }
        if (dto.getTaskId() != null) {
            wrapper.eq(TaskOrder::getTaskId, dto.getTaskId());
        }
        if (dto.getUserId() != null) {
            wrapper.eq(TaskOrder::getUserId, dto.getUserId());
        }
        wrapper.orderByDesc(TaskOrder::getCreateTime);

        IPage<TaskOrder> iPage = taskOrderMapper.selectPage(page, wrapper);
        List<TaskOrderVO> voList = convertToVOList(iPage.getRecords());

        return PageResult.of(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), voList);
    }

    @Override
    public List<TaskOrderVO> getMyOrders(Long userId, Integer status) {
        LambdaQueryWrapper<TaskOrder> wrapper = new LambdaQueryWrapper<TaskOrder>()
                .eq(TaskOrder::getUserId, userId);
        if (status != null) {
            wrapper.eq(TaskOrder::getAuditStatus, status);
        }
        wrapper.orderByDesc(TaskOrder::getCreateTime);
        List<TaskOrder> orders = taskOrderMapper.selectList(wrapper);
        return convertToVOList(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(OrderAuditDTO dto, Long auditId) {
        TaskOrder order = taskOrderMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "订单不存在");
        }
        // 校验订单状态为待审核
        if (order.getAuditStatus() != 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该订单已审核过，无法重复审核");
        }

        if (dto.getAuditStatus() == 1) {
            // 审核通过
            TaskInfo task = taskInfoService.getTaskById(order.getTaskId());

            // 计算佣金
            BigDecimal commission = calculateCommission(order, task);

            order.setRealCommission(commission);
            order.setAuditStatus(1);
            order.setAuditId(auditId);
            order.setAuditTime(LocalDateTime.now());
            taskOrderMapper.updateById(order);

            // 结算佣金 + 分润
            settlementCommission(order, task);

        } else if (dto.getAuditStatus() == 2) {
            // 驳回
            order.setAuditStatus(2);
            order.setAuditId(auditId);
            order.setAuditTime(LocalDateTime.now());
            order.setRejectReason(dto.getRejectReason());
            taskOrderMapper.updateById(order);
        } else {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "无效的审核状态");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        TaskOrder order = taskOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "订单不存在");
        }
        if (order.getAuditStatus() == 4) {
            return; // 已经取消
        }
        // 更新状态为已取消
        order.setAuditStatus(4);
        taskOrderMapper.updateById(order);

        // 恢复库存
        taskInfoMapper.increaseStock(order.getTaskId());
        log.info("订单 {} 已取消，库存已恢复", order.getOrderNo());
    }

    // ========== 私有方法 ==========

    /**
     * 生成订单号：日期+雪花ID后6位
     */
    private String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String snowflakeId = IdUtil.getSnowflake(1, 1).nextIdStr();
        String suffix = snowflakeId.length() > 6 ? snowflakeId.substring(snowflakeId.length() - 6) : snowflakeId;
        return datePart + suffix;
    }

    /**
     * 计算佣金
     * 固定佣金直接用 unitPrice，阶梯佣金从 ladderCommission JSON中匹配
     */
    private BigDecimal calculateCommission(TaskOrder order, TaskInfo task) {
        BigDecimal unitPrice = task.getUnitPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            unitPrice = BigDecimal.ZERO;
        }

        // 如果有阶梯佣金，尝试从 ladderCommission JSON中匹配
        if (task.getLadderCommission() != null && !task.getLadderCommission().isEmpty()) {
            try {
                // 统计用户已完成该任务的订单数
                int userCompletedCount = taskOrderMapper.countUserTaskOrders(order.getUserId(), order.getTaskId());
                // 解析阶梯佣金JSON，格式: [{"minCount":1,"maxCount":5,"price":10},{"minCount":6,"maxCount":10,"price":12}]
                com.alibaba.fastjson2.JSONArray ladderArray = com.alibaba.fastjson2.JSON.parseArray(task.getLadderCommission());
                for (int i = 0; i < ladderArray.size(); i++) {
                    com.alibaba.fastjson2.JSONObject step = ladderArray.getJSONObject(i);
                    int minCount = step.getIntValue("minCount");
                    int maxCount = step.getIntValue("maxCount");
                    if (userCompletedCount >= minCount && userCompletedCount < maxCount) {
                        BigDecimal price = step.getBigDecimal("price");
                        if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                            return price;
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("解析阶梯佣金失败: {}", e.getMessage());
            }
        }
        return unitPrice;
    }

    /**
     * 佣金结算（审核通过后调用）
     */
    private void settlementCommission(TaskOrder order, TaskInfo task) {
        BigDecimal commission = order.getRealCommission();
        if (commission == null || commission.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        // 更新用户余额和累计收益
        UserMember member = userMemberMapper.selectById(order.getUserId());
        if (member == null) {
            log.error("结算佣金时找不到用户: {}", order.getUserId());
            return;
        }

        member.setBalance(member.getBalance() != null ? member.getBalance().add(commission) : commission);
        member.setTotalIncome(member.getTotalIncome() != null ? member.getTotalIncome().add(commission) : commission);
        userMemberMapper.updateById(member);

        // 写入 user_income 流水
        UserIncome income = new UserIncome();
        income.setUserId(order.getUserId());
        income.setOrderId(order.getId());
        income.setTaskId(order.getTaskId());
        income.setAmount(commission);
        income.setIncomeType(1); // 任务佣金
        income.setDescription("完成任务【" + task.getTaskName() + "】获得佣金");
        income.setBalanceAfter(member.getBalance());
        userIncomeMapper.insert(income);

        // 调用分润
        distributeProfit(order, task);

        // 更新结算时间
        order.setSettleTime(LocalDateTime.now());
        taskOrderMapper.updateById(order);
    }

    /**
     * 团队分润 - 递归向上查找上级（层级限制3层）
     */
    private void distributeProfit(TaskOrder order, TaskInfo task) {
        if (task.getProfitRatio() == null || task.getProfitRatio().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal commission = order.getRealCommission();
        if (commission == null || commission.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        // 递归向上分润，最多3层
        distributeProfitRecursive(order.getUserId(), order.getId(), task.getId(),
                commission, task.getProfitRatio(), 1, 3);
    }

    /**
     * 递归分润
     *
     * @param childUserId 下级用户ID
     * @param orderId     订单ID
     * @param taskId      任务ID
     * @param commission  佣金基数
     * @param profitRatio 基础分润比例
     * @param level       当前层级
     * @param maxLevel    最大层级
     */
    private void distributeProfitRecursive(Long childUserId, Long orderId, Long taskId,
                                           BigDecimal commission, BigDecimal profitRatio,
                                           int level, int maxLevel) {
        if (level > maxLevel) {
            return;
        }

        // 查询下级用户的上级代理
        UserMember childMember = userMemberMapper.selectById(childUserId);
        if (childMember == null || childMember.getParentId() == null || childMember.getParentId() <= 0) {
            return;
        }

        Long parentId = childMember.getParentId();
        UserMember parentMember = userMemberMapper.selectById(parentId);
        if (parentMember == null) {
            return;
        }

        // 每层比例递减：第一层100%，第二层50%，第三层25%
        BigDecimal levelRatio = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(2, level - 1)), 4, java.math.RoundingMode.HALF_UP);
        BigDecimal actualRatio = profitRatio.multiply(levelRatio).divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);

        // 计算分润金额
        BigDecimal profitAmount = commission.multiply(actualRatio).setScale(2, java.math.RoundingMode.HALF_DOWN);

        if (profitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // 继续向上递归查找上级
            distributeProfitRecursive(parentId, orderId, taskId, commission, profitRatio, level + 1, maxLevel);
            return;
        }

        // 上级余额增加
        parentMember.setBalance(parentMember.getBalance() != null ? parentMember.getBalance().add(profitAmount) : profitAmount);
        userMemberMapper.updateById(parentMember);

        // 写入 income 流水（类型=团队分润）
        UserIncome income = new UserIncome();
        income.setUserId(parentId);
        income.setOrderId(orderId);
        income.setTaskId(taskId);
        income.setAmount(profitAmount);
        income.setIncomeType(2); // 团队分润
        income.setDescription("下级用户完成订单，第" + level + "层分润");
        income.setBalanceAfter(parentMember.getBalance());
        userIncomeMapper.insert(income);

        // 写入 user_team_profit 分润记录
        UserTeamProfit teamProfit = new UserTeamProfit();
        teamProfit.setUserId(parentId);
        teamProfit.setFromUserId(childUserId);
        teamProfit.setOrderId(orderId);
        teamProfit.setTaskId(taskId);
        teamProfit.setProfitAmount(profitAmount);
        teamProfit.setProfitRatio(profitRatio.multiply(levelRatio));
        teamProfit.setProfitLevel(level);
        userTeamProfitMapper.insert(teamProfit);

        // 递归向上查找上级
        distributeProfitRecursive(parentId, orderId, taskId, commission, profitRatio, level + 1, maxLevel);
    }

    /**
     * 将订单列表转换为VO列表
     */
    private List<TaskOrderVO> convertToVOList(List<TaskOrder> orders) {
        return orders.stream().map(order -> {
            TaskOrderVO vo = BeanUtil.copyProperties(order, TaskOrderVO.class);

            // 查询用户昵称和手机号
            UserMember member = userMemberMapper.selectById(order.getUserId());
            if (member != null) {
                vo.setUserNickname(member.getNickname());
                vo.setUserPhone(member.getPhone());
            }

            // 查询任务名称
            TaskInfo task = taskInfoMapper.selectById(order.getTaskId());
            if (task != null) {
                vo.setTaskName(task.getTaskName());
            }

            return vo;
        }).collect(Collectors.toList());
    }
}
