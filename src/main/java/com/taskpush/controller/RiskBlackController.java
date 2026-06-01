package com.taskpush.controller;

import com.taskpush.common.Result;
import com.taskpush.dto.RiskBlackDTO;
import com.taskpush.service.ISysRiskBlackService;
import com.taskpush.vo.SysRiskBlackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风控黑名单管理控制器
 */
@RestController
@RequestMapping("/api/admin/risk")
public class RiskBlackController {

    @Autowired
    private ISysRiskBlackService sysRiskBlackService;

    /**
     * 黑名单列表
     */
    @GetMapping("/list")
    public Result<List<SysRiskBlackVO>> list() {
        List<SysRiskBlackVO> list = sysRiskBlackService.list();
        return Result.success(list);
    }

    /**
     * 添加到黑名单
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated RiskBlackDTO dto) {
        sysRiskBlackService.add(dto);
        return Result.success(null);
    }

    /**
     * 移除黑名单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysRiskBlackService.remove(id);
        return Result.success(null);
    }
}
