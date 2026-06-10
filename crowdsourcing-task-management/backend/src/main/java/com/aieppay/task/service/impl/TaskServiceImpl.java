package com.aieppay.task.service.impl;

import com.aieppay.task.dto.request.TaskCreateRequest;
import com.aieppay.task.dto.response.TaskListResponse;
import com.aieppay.task.entity.TaskInfo;
import com.aieppay.task.entity.UserBalance;
import com.aieppay.task.entity.enums.TaskStatus;
import com.aieppay.task.exception.ServiceException;
import com.aieppay.task.mapper.TaskInfoMapper;
import com.aieppay.task.mapper.UserBalanceMapper;
import com.aieppay.task.service.TaskService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired

    private TaskInfoMapper taskInfoMapper;
    @Autowired
    private UserBalanceMapper userBalanceMapper;

    @Override
    @Transactional
    public Long createTask(Long employerId, TaskCreateRequest request) {
        TaskInfo task = new TaskInfo();
        task.setTitle(request.getTitle());
        task.setCategory(request.getCategory());
        task.setDescription(request.getDescription());
        task.setSteps(request.getSteps());
        task.setExampleImages(JSON.toJSONString(request.getExampleImages()));
        task.setDeadline(request.getDeadline());
        task.setCommission(request.getCommission());
        task.setMaxWorkers(request.getMaxWorkers());
        task.setTotalBudget(request.getTotalBudget());
        task.setAcceptanceCriteria(request.getAcceptanceCriteria());
        task.setDeliveryType(request.getDeliveryType());
        task.setRejectRules(request.getRejectRules());
        task.setPermissionSettings(JSON.toJSONString(request.getPermissionSettings()));
        task.setEmployerId(employerId);
        task.setStatus(TaskStatus.DRAFT.getCode());
        task.setAuditStatus(0);
        task.setAutoAuditHours(request.getAutoAuditHours() != null ? request.getAutoAuditHours() : 24);

        taskInfoMapper.insert(task);
        return task.getId();
    }

    @Override
    @Transactional
    public void updateTask(Long employerId, Long taskId, TaskCreateRequest request) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }
        if (!task.getEmployerId().equals(employerId)) {
            throw ServiceException.of(403, "无权限操作此任务");
        }

        task.setTitle(request.getTitle());
        task.setCategory(request.getCategory());
        task.setDescription(request.getDescription());
        task.setSteps(request.getSteps());
        task.setExampleImages(JSON.toJSONString(request.getExampleImages()));
        task.setDeadline(request.getDeadline());
        task.setCommission(request.getCommission());
        task.setMaxWorkers(request.getMaxWorkers());
        task.setTotalBudget(request.getTotalBudget());
        task.setAcceptanceCriteria(request.getAcceptanceCriteria());
        task.setDeliveryType(request.getDeliveryType());
        task.setRejectRules(request.getRejectRules());
        task.setPermissionSettings(JSON.toJSONString(request.getPermissionSettings()));
        taskInfoMapper.updateById(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long employerId, Long taskId) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }
        if (!task.getEmployerId().equals(employerId)) {
            throw ServiceException.of(403, "无权限操作此任务");
        }

        taskInfoMapper.deleteById(taskId);
    }

    @Override
    @Transactional
    public void publishTask(Long employerId, Long taskId) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }
        if (!task.getEmployerId().equals(employerId)) {
            throw ServiceException.of(403, "无权限操作此任务");
        }

        UserBalance balance = userBalanceMapper.selectByUserId(employerId);
        if (balance == null || balance.getAvailableBalance().compareTo(task.getTotalBudget()) < 0) {
            throw ServiceException.of("余额不足");
        }

        balance.setAvailableBalance(balance.getAvailableBalance().subtract(task.getTotalBudget()));
        balance.setFrozenBalance(balance.getFrozenBalance().add(task.getTotalBudget()));
        userBalanceMapper.updateById(balance);

        task.setStatus(TaskStatus.PENDING_AUDIT.getCode());
        taskInfoMapper.updateById(task);
    }

    @Override
    @Transactional
    public void offlineTask(Long employerId, Long taskId) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }
        if (!task.getEmployerId().equals(employerId)) {
            throw ServiceException.of(403, "无权限操作此任务");
        }

        task.setStatus(TaskStatus.OFFLINE.getCode());
        taskInfoMapper.updateById(task);
    }

    @Override
    public List<TaskListResponse> getOnlineTasks(Integer category, String keyword) {
        List<TaskInfo> tasks = taskInfoMapper.selectOnlineTasks();
        return convertToListResponse(tasks);
    }

    @Override
    public TaskListResponse getTaskDetail(Long taskId) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }
        return convertToResponse(task);
    }

    @Override
    public List<TaskListResponse> getEmployerTasks(Long employerId, Integer status) {
        List<TaskInfo> tasks = taskInfoMapper.selectByEmployerId(employerId);
        return convertToListResponse(tasks);
    }

    @Override
    @Transactional
    public void platformAuditTask(Long taskId, Integer result, String rejectReason) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }

        task.setAuditStatus(result);
        if (result == 2) {
            task.setRejectReason(rejectReason);
            task.setStatus(TaskStatus.OFFLINE.getCode());
        } else {
            task.setStatus(TaskStatus.ONLINE.getCode());
        }
        taskInfoMapper.updateById(task);
    }

    @Override
    public List<TaskListResponse> getPendingAuditTasks() {
        List<TaskInfo> tasks = taskInfoMapper.selectPendingAuditTasks();
        return convertToListResponse(tasks);
    }

    private List<TaskListResponse> convertToListResponse(List<TaskInfo> tasks) {
        List<TaskListResponse> response = new ArrayList<>();
        for (TaskInfo task : tasks) {
            response.add(convertToResponse(task));
        }
        return response;
    }

    private TaskListResponse convertToResponse(TaskInfo task) {
        TaskListResponse response = new TaskListResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setCategory(task.getCategory());
        response.setDescription(task.getDescription());
        response.setDeadline(task.getDeadline());
        response.setCommission(task.getCommission());
        response.setMaxWorkers(task.getMaxWorkers());
        
        int accepted = taskInfoMapper.countAcceptedOrders(task.getId());
        response.setRemainingWorkers(task.getMaxWorkers() - accepted);
        
        response.setExampleImages(JSON.parseArray(task.getExampleImages(), String.class));
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}