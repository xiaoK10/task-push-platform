package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taskpush.common.BusinessException;
import com.taskpush.common.PageResult;
import com.taskpush.common.ResultCode;
import com.taskpush.dto.WithdrawApplyDTO;
import com.taskpush.dto.WithdrawAuditDTO;
import com.taskpush.dto.WithdrawQueryDTO;
import com.taskpush.entity.UserMember;
import com.taskpush.entity.UserWithdraw;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.mapper.UserWithdrawMapper;
import com.taskpush.service.IUserWithdrawService;
import com.taskpush.vo.UserWithdrawVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户提现服务实现类
 */
@Slf4j
@Service
public class UserWithdrawServiceImpl implements IUserWithdrawService {

    @Autowired
    private UserWithdrawMapper userWithdrawMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    /** 默认手续费率(百分比) */
    private static final BigDecimal DEFAULT_FEE_RATE = BigDecimal.ZERO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(WithdrawApplyDTO dto, Long userId) {
        UserMember member = userMemberMapper.selectById(userId);
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        if (member.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "用户已被拉黑，无法提现");
        }
        if (member.getStatus() == 2) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "用户已被冻结，无法提现");
        }

        BigDecimal amount = dto.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "提现金额必须大于0");
        }

        // 校验余额 >= 提现金额
        BigDecimal balance = member.getBalance() != null ? member.getBalance() : BigDecimal.ZERO;
        if (balance.compareTo(amount) < 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "余额不足");
        }

        // 计算手续费（读取系统配置，默认0）
        BigDecimal feeRate = DEFAULT_FEE_RATE;
        BigDecimal fee = amount.multiply(feeRate).divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal realAmount = amount.subtract(fee);

        // 冻结用户余额
        member.setBalance(balance.subtract(amount));
        BigDecimal frozenBalance = member.getFrozenBalance() != null ? member.getFrozenBalance() : BigDecimal.ZERO;
        member.setFrozenBalance(frozenBalance.add(amount));
        userMemberMapper.updateById(member);

        // 生成提现单号
        String withdrawNo = generateWithdrawNo();

        UserWithdraw withdraw = new UserWithdraw();
        withdraw.setWithdrawNo(withdrawNo);
        withdraw.setUserId(userId);
        withdraw.setAmount(amount);
        withdraw.setFee(fee);
        withdraw.setRealAmount(realAmount);
        withdraw.setWithdrawType(dto.getWithdrawType());
        withdraw.setReceiveAccount(dto.getReceiveAccount());
        withdraw.setReceiveName(dto.getReceiveName());
        withdraw.setBankName(dto.getBankName());
        withdraw.setBankBranch(dto.getBankBranch());
        withdraw.setStatus(0); // 待审核

        userWithdrawMapper.insert(withdraw);
        log.info("用户 {} 提现申请成功，单号: {}，金额: {}", userId, withdrawNo, amount);
    }

    @Override
    public PageResult<UserWithdrawVO> pageQuery(WithdrawQueryDTO dto) {
        Page<UserWithdraw> page = new Page<>(
                dto.getPageNum() != null ? dto.getPageNum() : 1,
                dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<UserWithdraw> wrapper = new LambdaQueryWrapper<>();
        if (dto.getUserId() != null) {
            wrapper.eq(UserWithdraw::getUserId, dto.getUserId());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(UserWithdraw::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(UserWithdraw::getCreateTime);

        IPage<UserWithdraw> iPage = userWithdrawMapper.selectPage(page, wrapper);
        List<UserWithdrawVO> voList = convertToVOList(iPage.getRecords());

        return PageResult.of(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), voList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(WithdrawAuditDTO dto, Long auditId) {
        UserWithdraw withdraw = userWithdrawMapper.selectById(dto.getWithdrawId());
        if (withdraw == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "提现记录不存在");
        }
        if (withdraw.getStatus() != 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该提现已审核过，无法重复审核");
        }

        if (dto.getStatus() == 1) {
            // 审核通过：解冻余额，累加totalWithdraw
            UserMember member = userMemberMapper.selectById(withdraw.getUserId());
            if (member != null) {
                BigDecimal frozenBalance = member.getFrozenBalance() != null ? member.getFrozenBalance() : BigDecimal.ZERO;
                member.setFrozenBalance(frozenBalance.subtract(withdraw.getAmount()));
                BigDecimal totalWithdraw = member.getTotalWithdraw() != null ? member.getTotalWithdraw() : BigDecimal.ZERO;
                member.setTotalWithdraw(totalWithdraw.add(withdraw.getAmount()));
                userMemberMapper.updateById(member);
            }

            withdraw.setStatus(1);
            withdraw.setAuditId(auditId);
            withdraw.setAuditTime(LocalDateTime.now());
            withdraw.setPayTime(LocalDateTime.now());

        } else if (dto.getStatus() == 2) {
            // 驳回：退回余额
            UserMember member = userMemberMapper.selectById(withdraw.getUserId());
            if (member != null) {
                BigDecimal balance = member.getBalance() != null ? member.getBalance() : BigDecimal.ZERO;
                member.setBalance(balance.add(withdraw.getAmount()));
                BigDecimal frozenBalance = member.getFrozenBalance() != null ? member.getFrozenBalance() : BigDecimal.ZERO;
                member.setFrozenBalance(frozenBalance.subtract(withdraw.getAmount()));
                userMemberMapper.updateById(member);
            }

            withdraw.setStatus(2);
            withdraw.setAuditId(auditId);
            withdraw.setAuditTime(LocalDateTime.now());
            withdraw.setRejectReason(dto.getRejectReason());

        } else {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "无效的审核状态");
        }

        userWithdrawMapper.updateById(withdraw);
    }

    @Override
    public List<UserWithdrawVO> getMyWithdrawHistory(Long userId) {
        LambdaQueryWrapper<UserWithdraw> wrapper = new LambdaQueryWrapper<UserWithdraw>()
                .eq(UserWithdraw::getUserId, userId)
                .orderByDesc(UserWithdraw::getCreateTime);
        List<UserWithdraw> list = userWithdrawMapper.selectList(wrapper);
        return convertToVOList(list);
    }

    /**
     * 生成提现单号
     */
    private String generateWithdrawNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String snowflakeId = IdUtil.getSnowflake(1, 1).nextIdStr();
        String suffix = snowflakeId.length() > 6 ? snowflakeId.substring(snowflakeId.length() - 6) : snowflakeId;
        return "W" + datePart + suffix;
    }

    /**
     * 转换为VO列表
     */
    private List<UserWithdrawVO> convertToVOList(List<UserWithdraw> list) {
        return list.stream().map(w -> {
            UserWithdrawVO vo = BeanUtil.copyProperties(w, UserWithdrawVO.class);
            UserMember member = userMemberMapper.selectById(w.getUserId());
            if (member != null) {
                vo.setUserNickname(member.getNickname());
                vo.setUserPhone(member.getPhone());
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
