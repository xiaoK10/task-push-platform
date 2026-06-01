package com.taskpush.controller;

import com.taskpush.common.Result;
import com.taskpush.dto.LoginDTO;
import com.taskpush.service.ISysAdminService;
import com.taskpush.vo.AdminVO;
import com.taskpush.vo.LoginVO;
import com.taskpush.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统认证控制器
 */
@RestController
@RequestMapping("/api/admin")
public class SysAuthController {

    @Autowired
    private ISysAdminService sysAdminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO dto) {
        LoginVO loginVO = sysAdminService.login(dto);
        return Result.success(loginVO);
    }

    /**
     * 获取当前用户菜单树
     */
    @GetMapping("/menus")
    public Result<List<MenuVO>> menus() {
        Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MenuVO> menus = sysAdminService.getUserMenus(adminId);
        return Result.success(menus);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<AdminVO> info() {
        Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AdminVO adminVO = new AdminVO();
        adminVO.setId(adminId);
        adminVO.setUsername((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        return Result.success(adminVO);
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityContextHolder.clearContext();
        return Result.success(null);
    }
}
