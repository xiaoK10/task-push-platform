package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taskpush.common.BusinessException;
import com.taskpush.common.PageResult;
import com.taskpush.common.ResultCode;
import com.taskpush.common.util.JwtUtil;
import com.taskpush.dto.AdminQueryDTO;
import com.taskpush.dto.LoginDTO;
import com.taskpush.entity.SysAdmin;
import com.taskpush.entity.SysMenu;
import com.taskpush.entity.SysRole;
import com.taskpush.mapper.SysAdminMapper;
import com.taskpush.mapper.SysMenuMapper;
import com.taskpush.mapper.SysRoleMapper;
import com.taskpush.service.ISysAdminService;
import com.taskpush.vo.AdminVO;
import com.taskpush.vo.LoginVO;
import com.taskpush.vo.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员服务实现类
 */
@Slf4j
@Service
public class SysAdminServiceImpl implements ISysAdminService {

    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginDTO dto) {
        // 查询用户
        SysAdmin admin = sysAdminMapper.selectOne(
                new LambdaQueryWrapper<SysAdmin>()
                        .eq(SysAdmin::getUsername, dto.getUsername())
        );
        if (admin == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名或密码错误");
        }
        if (admin.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "账号已被禁用");
        }
        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名或密码错误");
        }
        // 生成JWT token
        String token = jwtUtil.createToken(admin.getId(), admin.getUsername());
        // 查询角色名称
        String roleName = null;
        if (admin.getRoleId() != null) {
            SysRole role = sysRoleMapper.selectById(admin.getRoleId());
            if (role != null) {
                roleName = role.getRoleName();
            }
        }
        // 构建菜单树
        List<MenuVO> menus = getUserMenus(admin.getId());
        // 组装返回
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setAdminId(admin.getId());
        loginVO.setUsername(admin.getUsername());
        loginVO.setRealName(admin.getRealName());
        loginVO.setRoleName(roleName);
        loginVO.setMenus(menus);
        return loginVO;
    }

    @Override
    public PageResult<AdminVO> listAdmins(AdminQueryDTO dto) {
        Page<SysAdmin> page = new Page<>(dto.getPageNum() != null ? dto.getPageNum() : 1,
                dto.getPageSize() != null ? dto.getPageSize() : 10);
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            wrapper.like(SysAdmin::getUsername, dto.getUsername());
        }
        if (dto.getRealName() != null && !dto.getRealName().isEmpty()) {
            wrapper.like(SysAdmin::getRealName, dto.getRealName());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysAdmin::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(SysAdmin::getCreateTime);
        IPage<SysAdmin> iPage = sysAdminMapper.selectPage(page, wrapper);

        // 收集所有roleId，批量查询角色名称
        List<Long> roleIds = iPage.getRecords().stream()
                .map(SysAdmin::getRoleId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> roleNameMap = sysRoleMapper.selectBatchIds(roleIds).stream()
                .collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName));

        List<AdminVO> voList = iPage.getRecords().stream().map(admin -> {
            AdminVO vo = BeanUtil.copyProperties(admin, AdminVO.class);
            if (admin.getRoleId() != null) {
                vo.setRoleName(roleNameMap.get(admin.getRoleId()));
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), voList);
    }

    @Override
    public List<MenuVO> getUserMenus(Long adminId) {
        List<SysMenu> menuList = sysMenuMapper.selectMenuTreeByAdminId(adminId);
        // 递归构建菜单树
        return buildMenuTree(menuList, 0L);
    }

    /**
     * 递归构建菜单树
     */
    private List<MenuVO> buildMenuTree(List<SysMenu> allMenus, Long parentId) {
        List<MenuVO> tree = new ArrayList<>();
        for (SysMenu menu : allMenus) {
            if (menu.getParentId().equals(parentId)) {
                MenuVO vo = BeanUtil.copyProperties(menu, MenuVO.class);
                List<MenuVO> children = buildMenuTree(allMenus, menu.getId());
                vo.setChildren(children.isEmpty() ? null : children);
                tree.add(vo);
            }
        }
        return tree;
    }
}
