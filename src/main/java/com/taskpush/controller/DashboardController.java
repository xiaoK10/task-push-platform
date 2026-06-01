package com.taskpush.controller;

import com.taskpush.common.Result;
import com.taskpush.service.IDashboardService;
import com.taskpush.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/statistics")
    public Result<DashboardVO> statistics() {
        DashboardVO vo = dashboardService.getDashboard();
        return Result.success(vo);
    }
}
