package com.taskpush.controller;

import com.taskpush.common.PageResult;
import com.taskpush.common.Result;
import com.taskpush.dto.OrderAuditDTO;
import com.taskpush.dto.OrderQueryDTO;
import com.taskpush.service.ITaskOrderService;
import com.taskpush.vo.TaskOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务订单审核控制器
 */
@RestController
@RequestMapping("/api/admin/order")
public class TaskOrderController {

    @Autowired
    private ITaskOrderService taskOrderService;

    /**
     * 分页查询订单列表
     */
    @GetMapping("/list")
    public Result<PageResult<TaskOrderVO>> list(OrderQueryDTO dto) {
        PageResult<TaskOrderVO> pageResult = taskOrderService.pageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * 订单详情
     */
    @GetMapping("/{id}")
    public Result<TaskOrderVO> getById(@PathVariable Long id) {
        TaskOrderVO vo = new TaskOrderVO();
        vo.setId(id);
        return Result.success(vo);
    }

    /**
     * 审核订单（通过/驳回）
     */
    @PostMapping("/audit")
    public Result<Void> audit(@RequestBody @Validated OrderAuditDTO dto) {
        Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskOrderService.audit(dto, adminId);
        return Result.success(null);
    }

    /**
     * 手动取消订单
     */
    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        taskOrderService.cancelOrder(id);
        return Result.success(null);
    }
}
