package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskpush.entity.UserIncome;
import com.taskpush.entity.UserMember;
import com.taskpush.mapper.UserIncomeMapper;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.service.IUserIncomeService;
import com.taskpush.vo.UserIncomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收益流水服务实现类
 */
@Service
public class UserIncomeServiceImpl implements IUserIncomeService {

    @Autowired
    private UserIncomeMapper userIncomeMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Override
    public List<UserIncomeVO> pageQuery(Long userId, Integer incomeType) {
        LambdaQueryWrapper<UserIncome> wrapper = new LambdaQueryWrapper<UserIncome>()
                .eq(UserIncome::getUserId, userId);
        if (incomeType != null) {
            wrapper.eq(UserIncome::getIncomeType, incomeType);
        }
        wrapper.orderByDesc(UserIncome::getCreateTime);

        List<UserIncome> incomes = userIncomeMapper.selectList(wrapper);

        // 查询用户昵称
        UserMember member = userMemberMapper.selectById(userId);

        return incomes.stream().map(income -> {
            UserIncomeVO vo = BeanUtil.copyProperties(income, UserIncomeVO.class);
            if (member != null) {
                vo.setUserNickname(member.getNickname());
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
