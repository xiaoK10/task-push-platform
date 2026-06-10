package com.aieppay.task.mapper;

import com.aieppay.task.entity.TaskInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {

    @Select("SELECT t.* FROM task_info t WHERE t.status = 2 AND t.audit_status = 1 AND t.deadline > NOW() ORDER BY t.created_at DESC")
    List<TaskInfo> selectOnlineTasks();

    @Select("SELECT t.* FROM task_info t WHERE t.employer_id = #{employerId} ORDER BY t.created_at DESC")
    List<TaskInfo> selectByEmployerId(@Param("employerId") Long employerId);

    @Select("SELECT t.* FROM task_info t WHERE t.audit_status = 0 ORDER BY t.created_at DESC")
    List<TaskInfo> selectPendingAuditTasks();

    @Select("SELECT t.* FROM task_info t WHERE t.status = #{status} ORDER BY t.created_at DESC")
    List<TaskInfo> selectByStatus(@Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM order_info o WHERE o.task_id = #{taskId} AND o.status NOT IN (6)")
    Integer countAcceptedOrders(@Param("taskId") Long taskId);
}