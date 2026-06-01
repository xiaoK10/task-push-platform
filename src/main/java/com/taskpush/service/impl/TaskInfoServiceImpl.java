package com.taskpush.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taskpush.common.BusinessException;
import com.taskpush.common.PageResult;
import com.taskpush.common.ResultCode;
import com.taskpush.dto.TaskQueryDTO;
import com.taskpush.dto.TaskSaveDTO;
import com.taskpush.entity.TaskCategory;
import com.taskpush.entity.TaskInfo;
import com.taskpush.mapper.TaskCategoryMapper;
import com.taskpush.mapper.TaskInfoMapper;
import com.taskpush.service.ITaskInfoService;
import com.taskpush.vo.TaskInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务信息服务实现类
 */
@Service
public class TaskInfoServiceImpl implements ITaskInfoService {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private TaskCategoryMapper taskCategoryMapper;

    @Override
    public PageResult<TaskInfoVO> pageQuery(TaskQueryDTO dto) {
        Page<TaskInfo> page = new Page<>(
                dto.getPageNum() != null ? dto.getPageNum() : 1,
                dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<TaskInfo> wrapper = new LambdaQueryWrapper<>();
        if (dto.getCategoryId() != null) {
            wrapper.eq(TaskInfo::getCategoryId, dto.getCategoryId());
        }
        if (dto.getTaskName() != null && !dto.getTaskName().isEmpty()) {
            wrapper.like(TaskInfo::getTaskName, dto.getTaskName());
        }
        if (dto.getTaskStatus() != null) {
            wrapper.eq(TaskInfo::getTaskStatus, dto.getTaskStatus());
        }
        if (dto.getStartTime() != null && !dto.getStartTime().isEmpty()) {
            wrapper.ge(TaskInfo::getCreateTime, dto.getStartTime());
        }
        if (dto.getEndTime() != null && !dto.getEndTime().isEmpty()) {
            wrapper.le(TaskInfo::getCreateTime, dto.getEndTime());
        }
        wrapper.orderByDesc(TaskInfo::getCreateTime);

        IPage<TaskInfo> iPage = taskInfoMapper.selectPage(page, wrapper);

        // 批量查询分类名称
        List<Long> categoryIds = iPage.getRecords().stream()
                .map(TaskInfo::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> categoryNameMap = taskCategoryMapper.selectBatchIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(TaskCategory::getId, TaskCategory::getCategoryName));

        List<TaskInfoVO> voList = iPage.getRecords().stream().map(task -> {
            TaskInfoVO vo = BeanUtil.copyProperties(task, TaskInfoVO.class);
            if (task.getCategoryId() != null) {
                vo.setCategoryName(categoryNameMap.get(task.getCategoryId()));
            }
            // 已抢数量 = 总库存 - 剩余库存
            vo.setGrabbedCount(task.getTotalStock() - task.getRemainStock());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), voList);
    }

    @Override
    public TaskInfoVO getById(Long id) {
        TaskInfo task = taskInfoMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "任务不存在");
        }
        TaskInfoVO vo = BeanUtil.copyProperties(task, TaskInfoVO.class);
        if (task.getCategoryId() != null) {
            TaskCategory category = taskCategoryMapper.selectById(task.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getCategoryName());
            }
        }
        vo.setGrabbedCount(task.getTotalStock() - task.getRemainStock());
        return vo;
    }

    @Override
    public void save(TaskSaveDTO dto) {
        TaskInfo task;
        if (dto.getId() != null) {
            task = taskInfoMapper.selectById(dto.getId());
            if (task == null) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "任务不存在");
            }
            BeanUtil.copyProperties(dto, task, "id", "createTime", "remainStock");
            // 如果编辑时修改了总库存，同步更新剩余库存
            if (dto.getTotalStock() != null && dto.getTotalStock() > 0) {
                int grabbedCount = task.getTotalStock() != null ? task.getTotalStock() - task.getRemainStock() : 0;
                task.setRemainStock(Math.max(dto.getTotalStock() - grabbedCount, 0));
            }
        } else {
            // 验证库存为正数
            if (dto.getTotalStock() == null || dto.getTotalStock() <= 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "总库存必须为正数");
            }
            // 验证时间合法性
            if (dto.getTaskStartTime() != null && dto.getTaskEndTime() != null) {
                if (dto.getTaskEndTime().isBefore(dto.getTaskStartTime())) {
                    throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "结束时间不能早于开始时间");
                }
            }
            task = BeanUtil.copyProperties(dto, TaskInfo.class);
            task.setRemainStock(dto.getTotalStock());
            task.setTaskStatus(0); // 草稿状态
        }
        if (dto.getMaxGrabPerUser() == null) {
            task.setMaxGrabPerUser(3);
        }
        if (dto.getGrabExpireHours() == null) {
            task.setGrabExpireHours(24);
        }

        if (dto.getId() != null) {
            taskInfoMapper.updateById(task);
        } else {
            taskInfoMapper.insert(task);
        }
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        TaskInfo task = taskInfoMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "任务不存在");
        }
        task.setTaskStatus(status);
        taskInfoMapper.updateById(task);
    }

    @Override
    public void delete(Long id) {
        TaskInfo task = taskInfoMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "任务不存在");
        }
        taskInfoMapper.deleteById(id);
    }

    @Override
    public TaskInfo getTaskById(Long id) {
        TaskInfo task = taskInfoMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "任务不存在");
        }
        return task;
    }
}
