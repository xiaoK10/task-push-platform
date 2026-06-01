package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.TaskCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 任务分类 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface TaskCategoryMapper extends BaseMapper<TaskCategory> {

    /**
     * 统计某分类下的任务数量
     *
     * @param categoryId 分类ID
     * @return 任务数量
     */
    @Select("SELECT COUNT(*) FROM task_info WHERE category_id = #{categoryId} AND is_deleted = 0")
    int countTaskByCategoryId(@Param("categoryId") Long categoryId);

}
