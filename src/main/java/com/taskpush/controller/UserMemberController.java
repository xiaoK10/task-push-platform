package com.taskpush.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskpush.common.PageResult;
import com.taskpush.common.Result;
import com.taskpush.dto.UserQueryDTO;
import com.taskpush.dto.UserUpdateDTO;
import com.taskpush.entity.UserMember;
import com.taskpush.mapper.UserMemberMapper;
import com.taskpush.service.IUserMemberService;
import com.taskpush.vo.UserMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/admin/user")
public class UserMemberController {

    @Autowired
    private IUserMemberService userMemberService;

    @Autowired
    private UserMemberMapper userMemberMapper;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Result<PageResult<UserMemberVO>> list(UserQueryDTO dto) {
        PageResult<UserMemberVO> pageResult = userMemberService.pageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * 用户详情
     */
    @GetMapping("/{id}")
    public Result<UserMemberVO> getById(@PathVariable Long id) {
        UserMemberVO vo = userMemberService.getById(id);
        return Result.success(vo);
    }

    /**
     * 编辑用户（等级/状态）
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody @Validated UserUpdateDTO dto) {
        userMemberService.updateUser(dto);
        return Result.success(null);
    }

    /**
     * 查询团队下级列表
     */
    @GetMapping("/team/{userId}")
    public Result<List<UserMemberVO>> team(@PathVariable Long userId) {
        LambdaQueryWrapper<UserMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMember::getParentId, userId);
        List<UserMember> members = userMemberMapper.selectList(wrapper);
        List<UserMemberVO> voList = members.stream().map(member -> {
            UserMemberVO vo = new UserMemberVO();
            vo.setId(member.getId());
            vo.setOpenid(member.getOpenid());
            vo.setPhone(member.getPhone());
            vo.setNickname(member.getNickname());
            vo.setAvatar(member.getAvatar());
            vo.setUserLevel(member.getUserLevel());
            vo.setProvince(member.getProvince());
            vo.setCity(member.getCity());
            vo.setDistrict(member.getDistrict());
            vo.setParentId(member.getParentId());
            vo.setBalance(member.getBalance());
            vo.setFrozenBalance(member.getFrozenBalance());
            vo.setTotalIncome(member.getTotalIncome());
            vo.setTotalWithdraw(member.getTotalWithdraw());
            vo.setStatus(member.getStatus());
            vo.setRegisterTime(member.getRegisterTime());
            int teamCount = userMemberMapper.countByParentId(member.getId());
            vo.setTeamCount(teamCount);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }
}
