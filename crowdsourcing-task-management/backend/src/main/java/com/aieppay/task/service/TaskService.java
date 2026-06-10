package com.aieppay.task.service;

import com.aieppay.task.dto.request.TaskCreateRequest;
import com.aieppay.task.dto.response.TaskListResponse;

import java.util.List;

public interface TaskService {

    Long createTask(Long employerId, TaskCreateRequest request);

    void updateTask(Long employerId, Long taskId, TaskCreateRequest request);

    void deleteTask(Long employerId, Long taskId);

    void publishTask(Long employerId, Long taskId);

    void offlineTask(Long employerId, Long taskId);

    List<TaskListResponse> getOnlineTasks(Integer category, String keyword);

    TaskListResponse getTaskDetail(Long taskId);

    List<TaskListResponse> getEmployerTasks(Long employerId, Integer status);

    void platformAuditTask(Long taskId, Integer result, String rejectReason);

    List<TaskListResponse> getPendingAuditTasks();
}