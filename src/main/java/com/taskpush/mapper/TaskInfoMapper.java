package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.TaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 任务 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {

    /**
     * 扣减库存（分布式锁后调用，原子操作）
     *
     * @param taskId 任务ID
     * @return 受影响行数（0表示库存不足）
     */
    @Update("UPDATE task_info SET remain_stock = remain_stock - 1 " +
            "WHERE id = #{taskId} AND remain_stock > 0")
    int decreaseStock(@Param("taskId") Long taskId);

    /**
     * 回滚库存（取消订单时调用）
     *
     * @param taskId 任务ID
     * @return 受影响行数
     */
    @Update("UPDATE task_info SET remain_stock = remain_stock + 1 " +
            "WHERE id = #{taskId}")
    int increaseStock(@Param("taskId") Long taskId);

}
