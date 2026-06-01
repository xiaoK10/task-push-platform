package com.taskpush.service;

import com.taskpush.vo.DashboardVO;

/**
 * 仪表盘服务接口
 */
public interface IDashboardService {

    /**
     * 获取仪表盘统计数据
     *
     * @return 仪表盘VO
     */
    DashboardVO getDashboard();
}
