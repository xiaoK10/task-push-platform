package com.taskpush.service;

import com.taskpush.common.PageResult;
import com.taskpush.dto.AdminQueryDTO;
import com.taskpush.dto.LoginDTO;
import com.taskpush.vo.AdminVO;
import com.taskpush.vo.LoginVO;
import com.taskpush.vo.MenuVO;

import java.util.List;

/**
 * 管理员服务接口
 */
public interface ISysAdminService {

    /**
     * 管理员登录
     *
     * @param dto 登录参数
     * @return 登录结果（含token和菜单树）
     */
    LoginVO login(LoginDTO dto);

    /**
     * 分页查询管理员列表
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageResult<AdminVO> listAdmins(AdminQueryDTO dto);

    /**
     * 获取用户菜单树
     *
     * @param adminId 管理员ID
     * @return 菜单树列表
     */
    List<MenuVO> getUserMenus(Long adminId);
}
