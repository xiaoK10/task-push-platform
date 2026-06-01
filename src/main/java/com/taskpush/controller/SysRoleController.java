package com.taskpush.controller;

import com.taskpush.common.Result;
import com.taskpush.entity.SysRole;
import com.taskpush.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色管理控制器
 */
@RestController
@RequestMapping("/api/admin/role")
public class SysRoleController {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 角色列表
     */
    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        List<SysRole> roles = sysRoleMapper.selectList(null);
        return Result.success(roles);
    }

    /**
     * 新增角色（预留）
     */
    @PostMapping("/add")
    public Result<Void> add() {
        return Result.success(null);
    }

    /**
     * 编辑角色（预留）
     */
    @PutMapping("/update")
    public Result<Void> update() {
        return Result.success(null);
    }
}
