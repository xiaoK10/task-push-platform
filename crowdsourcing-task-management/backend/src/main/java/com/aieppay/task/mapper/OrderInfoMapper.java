package com.aieppay.task.mapper;

import com.aieppay.task.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Select("SELECT o.* FROM order_info o WHERE o.task_id = #{taskId} ORDER BY o.created_at DESC")
    List<OrderInfo> selectByTaskId(@Param("taskId") Long taskId);

    @Select("SELECT o.* FROM order_info o WHERE o.worker_id = #{workerId} ORDER BY o.created_at DESC")
    List<OrderInfo> selectByWorkerId(@Param("workerId") Long workerId);

    @Select("SELECT o.* FROM order_info o WHERE o.employer_id = #{employerId} ORDER BY o.created_at DESC")
    List<OrderInfo> selectByEmployerId(@Param("employerId") Long employerId);

    @Select("SELECT o.* FROM order_info o WHERE o.status = #{status} ORDER BY o.created_at DESC")
    List<OrderInfo> selectByStatus(@Param("status") Integer status);

    @Select("SELECT o.* FROM order_info o WHERE o.employer_id = #{employerId} AND o.status = 3 ORDER BY o.delivery_time DESC")
    List<OrderInfo> selectPendingAuditByEmployer(@Param("employerId") Long employerId);

    @Select("SELECT COUNT(*) FROM order_info o WHERE o.worker_id = #{workerId} AND o.status IN (1, 2, 3)")
    Integer countOngoingOrders(@Param("workerId") Long workerId);
}