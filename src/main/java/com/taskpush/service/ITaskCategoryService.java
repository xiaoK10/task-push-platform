package com.taskpush.service;

import com.taskpush.dto.CategorySaveDTO;
import com.taskpush.vo.TaskCategoryVO;

import java.util.List;

/**
 * 任务分类服务接口
 */
public interface ITaskCategoryService {

    /**
     * 全部分类列表（含任务数量）
     *
     * @return 分类VO列表
     */
    List<TaskCategoryVO> list();

    /**
     * 新增/编辑分类
     *
     * @param dto 分类数据
     */
    void save(CategorySaveDTO dto);

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void delete(Long id);
}
