package com.taskpush.service;

import com.taskpush.common.PageResult;
import com.taskpush.dto.UserQueryDTO;
import com.taskpush.dto.UserUpdateDTO;
import com.taskpush.entity.UserMember;
import com.taskpush.vo.UserMemberVO;

/**
 * 用户服务接口
 */
public interface IUserMemberService {

    /**
     * 分页查询用户列表（含团队人数）
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageResult<UserMemberVO> pageQuery(UserQueryDTO dto);

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户VO
     */
    UserMemberVO getById(Long id);

    /**
     * 编辑用户等级、状态
     *
     * @param dto 更新参数
     */
    void updateUser(UserUpdateDTO dto);

    /**
     * 小程序注册
     *
     * @param openid 微信OpenID
     * @return 用户实体
     */
    UserMember registerByOpenid(String openid);

    /**
     * 绑定手机号
     *
     * @param userId 用户ID
     * @param phone  手机号
     */
    void bindPhone(Long userId, String phone);

    /**
     * 我的资料
     *
     * @param userId 用户ID
     * @return 用户VO
     */
    UserMemberVO getMyProfile(Long userId);
}
