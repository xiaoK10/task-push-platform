package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taskpush.common.BusinessException;
import com.taskpush.common.PageResult;
import com.taskpush.common.ResultCode;
import com.taskpush.dto.UserQueryDTO;
import com.taskpush.dto.UserUpdateDTO;
import com.taskpush.entity.UserMember;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.service.IUserMemberService;
import com.taskpush.vo.UserMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserMemberServiceImpl implements IUserMemberService {

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Override
    public PageResult<UserMemberVO> pageQuery(UserQueryDTO dto) {
        Page<UserMember> page = new Page<>(
                dto.getPageNum() != null ? dto.getPageNum() : 1,
                dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<UserMember> wrapper = new LambdaQueryWrapper<>();
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            wrapper.like(UserMember::getPhone, dto.getPhone());
        }
        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            wrapper.like(UserMember::getNickname, dto.getNickname());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(UserMember::getStatus, dto.getStatus());
        }
        if (dto.getUserLevel() != null) {
            wrapper.eq(UserMember::getUserLevel, dto.getUserLevel());
        }
        wrapper.orderByDesc(UserMember::getCreateTime);

        IPage<UserMember> iPage = userMemberMapper.selectPage(page, wrapper);

        List<UserMemberVO> voList = iPage.getRecords().stream().map(member -> {
            UserMemberVO vo = BeanUtil.copyProperties(member, UserMemberVO.class);
            // 统计团队人数
            int teamCount = userMemberMapper.countByParentId(member.getId());
            vo.setTeamCount(teamCount);
            // 查询上级名称
            if (member.getParentId() != null && member.getParentId() > 0) {
                UserMember parent = userMemberMapper.selectById(member.getParentId());
                if (parent != null) {
                    vo.setParentName(parent.getNickname());
                }
            }
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), voList);
    }

    @Override
    public UserMemberVO getById(Long id) {
        UserMember member = userMemberMapper.selectById(id);
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        UserMemberVO vo = BeanUtil.copyProperties(member, UserMemberVO.class);
        int teamCount = userMemberMapper.countByParentId(member.getId());
        vo.setTeamCount(teamCount);
        if (member.getParentId() != null && member.getParentId() > 0) {
            UserMember parent = userMemberMapper.selectById(member.getParentId());
            if (parent != null) {
                vo.setParentName(parent.getNickname());
            }
        }
        return vo;
    }

    @Override
    public void updateUser(UserUpdateDTO dto) {
        UserMember member = userMemberMapper.selectById(dto.getUserId());
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        if (dto.getUserLevel() != null) {
            member.setUserLevel(dto.getUserLevel());
        }
        if (dto.getStatus() != null) {
            member.setStatus(dto.getStatus());
        }
        userMemberMapper.updateById(member);
    }

    @Override
    public UserMember registerByOpenid(String openid) {
        // 检查是否已注册
        UserMember existMember = userMemberMapper.selectByOpenid(openid);
        if (existMember != null) {
            return existMember;
        }
        // 新用户注册
        UserMember member = new UserMember();
        member.setOpenid(openid);
        member.setUserLevel(0);
        member.setStatus(1);
        member.setBalance(java.math.BigDecimal.ZERO);
        member.setFrozenBalance(java.math.BigDecimal.ZERO);
        member.setTotalIncome(java.math.BigDecimal.ZERO);
        member.setTotalWithdraw(java.math.BigDecimal.ZERO);
        member.setRegisterTime(LocalDateTime.now());
        userMemberMapper.insert(member);
        return member;
    }

    @Override
    public void bindPhone(Long userId, String phone) {
        UserMember member = userMemberMapper.selectById(userId);
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        // 检查手机号是否已被其他用户绑定
        LambdaQueryWrapper<UserMember> wrapper = new LambdaQueryWrapper<UserMember>()
                .eq(UserMember::getPhone, phone)
                .ne(UserMember::getId, userId);
        Long count = userMemberMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该手机号已被其他用户绑定");
        }
        member.setPhone(phone);
        userMemberMapper.updateById(member);
    }

    @Override
    public UserMemberVO getMyProfile(Long userId) {
        UserMember member = userMemberMapper.selectById(userId);
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        UserMemberVO vo = BeanUtil.copyProperties(member, UserMemberVO.class);
        int teamCount = userMemberMapper.countByParentId(member.getId());
        vo.setTeamCount(teamCount);
        if (member.getParentId() != null && member.getParentId() > 0) {
            UserMember parent = userMemberMapper.selectById(member.getParentId());
            if (parent != null) {
                vo.setParentName(parent.getNickname());
            }
        }
        return vo;
    }
}
