package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskpush.common.BusinessException;
import com.taskpush.common.ResultCode;
import com.taskpush.common.constant.CommonConstant;
import com.taskpush.common.util.RedisUtil;
import com.taskpush.dto.RiskBlackDTO;
import com.taskpush.entity.SysRiskBlack;
import com.taskpush.mapper.SysRiskBlackMapper;
import com.taskpush.service.ISysRiskBlackService;
import com.taskpush.vo.SysRiskBlackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 风控黑名单服务实现类
 */
@Service
public class SysRiskBlackServiceImpl implements ISysRiskBlackService {

    @Autowired
    private SysRiskBlackMapper sysRiskBlackMapper;

    @Autowired
    private RedisUtil redisUtil;

    /** 黑名单Redis缓存过期时间(天) */
    private static final long BLACKLIST_CACHE_DAYS = 30;

    @Override
    public void add(RiskBlackDTO dto) {
        // 检查是否已存在
        LambdaQueryWrapper<SysRiskBlack> wrapper = new LambdaQueryWrapper<SysRiskBlack>()
                .eq(SysRiskBlack::getBlackType, dto.getBlackType())
                .eq(SysRiskBlack::getBlackValue, dto.getBlackValue());
        Long existCount = sysRiskBlackMapper.selectCount(wrapper);
        if (existCount != null && existCount > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该记录已存在于黑名单中");
        }

        SysRiskBlack black = new SysRiskBlack();
        black.setBlackType(dto.getBlackType());
        black.setBlackValue(dto.getBlackValue());
        black.setReason(dto.getReason());
        black.setStatus(1);
        sysRiskBlackMapper.insert(black);

        // 同时缓存到Redis
        String redisKey = CommonConstant.BLACKLIST_PREFIX + dto.getBlackType() + ":" + dto.getBlackValue();
        redisUtil.set(redisKey, "1", BLACKLIST_CACHE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void remove(Long id) {
        SysRiskBlack black = sysRiskBlackMapper.selectById(id);
        if (black == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "黑名单记录不存在");
        }
        sysRiskBlackMapper.deleteById(id);

        // 删除Redis缓存
        String redisKey = CommonConstant.BLACKLIST_PREFIX + black.getBlackType() + ":" + black.getBlackValue();
        redisUtil.delete(redisKey);
    }

    @Override
    public boolean checkBlack(String value, Integer type) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        // 先检查Redis缓存
        String redisKey = CommonConstant.BLACKLIST_PREFIX + type + ":" + value;
        String cached = redisUtil.getString(redisKey);
        if ("1".equals(cached)) {
            return true;
        }
        // Redis未命中，查DB
        LambdaQueryWrapper<SysRiskBlack> wrapper = new LambdaQueryWrapper<SysRiskBlack>()
                .eq(SysRiskBlack::getBlackType, type)
                .eq(SysRiskBlack::getBlackValue, value)
                .eq(SysRiskBlack::getStatus, 1);
        Long count = sysRiskBlackMapper.selectCount(wrapper);
        boolean isBlack = count != null && count > 0;

        // 缓存结果到Redis
        if (isBlack) {
            redisUtil.set(redisKey, "1", BLACKLIST_CACHE_DAYS, TimeUnit.DAYS);
        }

        return isBlack;
    }

    @Override
    public List<SysRiskBlackVO> list() {
        List<SysRiskBlack> list = sysRiskBlackMapper.selectList(
                new LambdaQueryWrapper<SysRiskBlack>()
                        .orderByDesc(SysRiskBlack::getCreateTime)
        );
        return list.stream()
                .map(b -> BeanUtil.copyProperties(b, SysRiskBlackVO.class))
                .collect(Collectors.toList());
    }
}
