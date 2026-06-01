package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.TaskOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 订单 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface TaskOrderMapper extends BaseMapper<TaskOrder> {

    /**
     * 统计用户对某个任务已抢单的数量
     *
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 已抢单数量
     */
    @Select("SELECT COUNT(*) FROM task_order " +
            "WHERE user_id = #{userId} AND task_id = #{taskId} " +
            "AND is_deleted = 0")
    int countUserTaskOrders(@Param("userId") Long userId, @Param("taskId") Long taskId);

}
