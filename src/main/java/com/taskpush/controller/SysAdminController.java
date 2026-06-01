package com.taskpush.controller;

import com.taskpush.common.PageResult;
import com.taskpush.common.Result;
import com.taskpush.dto.AdminQueryDTO;
import com.taskpush.service.ISysAdminService;
import com.taskpush.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理员管理控制器
 */
@RestController
@RequestMapping("/api/admin/manage")
public class SysAdminController {

    @Autowired
    private ISysAdminService sysAdminService;

    /**
     * 管理员列表（分页）
     */
    @GetMapping("/list")
    public Result<PageResult<AdminVO>> list(AdminQueryDTO dto) {
        PageResult<AdminVO> pageResult = sysAdminService.listAdmins(dto);
        return Result.success(pageResult);
    }

    /**
     * 新增管理员（预留）
     */
    @PostMapping("/add")
    public Result<Void> add() {
        return Result.success(null);
    }

    /**
     * 编辑管理员（预留）
     */
    @PutMapping("/update")
    public Result<Void> update() {
        return Result.success(null);
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(null);
    }
}
