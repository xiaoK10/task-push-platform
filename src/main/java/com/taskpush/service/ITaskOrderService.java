package com.taskpush.service;

import com.taskpush.common.PageResult;
import com.taskpush.dto.GrabTaskDTO;
import com.taskpush.dto.OrderAuditDTO;
import com.taskpush.dto.OrderQueryDTO;
import com.taskpush.dto.SubmitOrderDTO;
import com.taskpush.vo.TaskOrderVO;

import java.util.List;

/**
 * 任务订单服务接口
 */
public interface ITaskOrderService {

    /**
     * 抢单核心逻辑
     *
     * @param dto    抢单参数
     * @param userId 用户ID
     */
    void grabTask(GrabTaskDTO dto, Long userId);

    /**
     * 提交凭证
     *
     * @param dto    提交参数
     * @param userId 用户ID
     */
    void submitCert(SubmitOrderDTO dto, Long userId);

    /**
     * 分页查询订单（含用户昵称、任务名）
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageResult<TaskOrderVO> pageQuery(OrderQueryDTO dto);

    /**
     * 小程序端我的订单
     *
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    List<TaskOrderVO> getMyOrders(Long userId, Integer status);

    /**
     * 审核订单（通过/驳回）
     *
     * @param dto     审核参数
     * @param auditId 审核人ID
     */
    void audit(OrderAuditDTO dto, Long auditId);

    /**
     * 取消订单（定时任务调用）
     *
     * @param orderId 订单ID
     */
    void cancelOrder(Long orderId);
}
