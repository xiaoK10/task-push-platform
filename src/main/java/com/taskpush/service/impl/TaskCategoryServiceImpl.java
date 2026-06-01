package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taskpush.common.BusinessException;
import com.taskpush.common.ResultCode;
import com.taskpush.dto.CategorySaveDTO;
import com.taskpush.entity.TaskCategory;
import com.taskpush.mapper.TaskCategoryMapper;
import com.taskpush.mapper.TaskInfoMapper;
import com.taskpush.service.ITaskCategoryService;
import com.taskpush.vo.TaskCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务分类服务实现类
 */
@Service
public class TaskCategoryServiceImpl implements ITaskCategoryService {

    @Autowired
    private TaskCategoryMapper taskCategoryMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Override
    public List<TaskCategoryVO> list() {
        List<TaskCategory> categories = taskCategoryMapper.selectList(
                new LambdaQueryWrapper<TaskCategory>()
                        .eq(TaskCategory::getStatus, 1)
                        .orderByAsc(TaskCategory::getSort)
        );
        return categories.stream().map(cat -> {
            TaskCategoryVO vo = BeanUtil.copyProperties(cat, TaskCategoryVO.class);
            // 统计该分类下的任务数量
            Long taskCount = taskInfoMapper.selectCount(
                    new LambdaQueryWrapper<com.taskpush.entity.TaskInfo>()
                            .eq(com.taskpush.entity.TaskInfo::getCategoryId, cat.getId())
            );
            vo.setTaskCount(taskCount != null ? taskCount.intValue() : 0);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void save(CategorySaveDTO dto) {
        TaskCategory category;
        if (dto.getId() != null) {
            category = taskCategoryMapper.selectById(dto.getId());
            if (category == null) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "分类不存在");
            }
            BeanUtil.copyProperties(dto, category, "id", "createTime");
            taskCategoryMapper.updateById(category);
        } else {
            category = BeanUtil.copyProperties(dto, TaskCategory.class);
            category.setStatus(1);
            taskCategoryMapper.insert(category);
        }
    }

    @Override
    public void delete(Long id) {
        TaskCategory category = taskCategoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "分类不存在");
        }
        // 检查分类下是否有任务
        Long count = taskInfoMapper.selectCount(
                new LambdaQueryWrapper<com.taskpush.entity.TaskInfo>()
                        .eq(com.taskpush.entity.TaskInfo::getCategoryId, id)
        );
        if (count != null && count > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该分类下存在任务，无法删除");
        }
        taskCategoryMapper.deleteById(id);
    }
}
