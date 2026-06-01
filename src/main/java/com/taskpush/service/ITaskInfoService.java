package com.taskpush.service;

import com.taskpush.common.PageResult;
import com.taskpush.dto.TaskQueryDTO;
import com.taskpush.dto.TaskSaveDTO;
import com.taskpush.vo.TaskInfoVO;

/**
 * 任务信息服务接口
 */
public interface ITaskInfoService {

    /**
     * 分页查询任务，含分类名称
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageResult<TaskInfoVO> pageQuery(TaskQueryDTO dto);

    /**
     * 获取任务详情
     *
     * @param id 任务ID
     * @return 任务VO
     */
    TaskInfoVO getById(Long id);

    /**
     * 新增/编辑任务
     *
     * @param dto 任务数据
     */
    void save(TaskSaveDTO dto);

    /**
     * 更新任务状态（上下架/完结）
     *
     * @param id     任务ID
     * @param status 目标状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除任务
     *
     * @param id 任务ID
     */
    void delete(Long id);

    /**
     * 根据ID查询任务实体
     *
     * @param id 任务ID
     * @return 任务实体
     */
    com.taskpush.entity.TaskInfo getTaskById(Long id);
}
