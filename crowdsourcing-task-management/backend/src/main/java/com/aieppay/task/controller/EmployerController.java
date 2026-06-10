package com.aieppay.task.controller;

import com.aieppay.task.dto.request.OrderAuditRequest;
import com.aieppay.task.dto.request.TaskCreateRequest;
import com.aieppay.task.dto.response.ApiResponse;
import com.aieppay.task.dto.response.OrderListResponse;
import com.aieppay.task.dto.response.TaskListResponse;
import com.aieppay.task.service.FundService;
import com.aieppay.task.service.OrderService;
import com.aieppay.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer")
@RequiredArgsConstructor
public class EmployerController {

    private final TaskService taskService;
    private final OrderService orderService;
    private final FundService fundService;

    @PostMapping("/{employerId}/tasks")
    public ResponseEntity<ApiResponse<Long>> createTask(@PathVariable Long employerId,
                                                       @Valid @RequestBody TaskCreateRequest request) {
        Long taskId = taskService.createTask(employerId, request);
        return ResponseEntity.ok(ApiResponse.success(taskId));
    }

    @PutMapping("/{employerId}/tasks/{taskId}")
    public ResponseEntity<ApiResponse<Void>> updateTask(@PathVariable Long employerId,
                                                       @PathVariable Long taskId,
                                                       @Valid @RequestBody TaskCreateRequest request) {
        taskService.updateTask(employerId, taskId, request);
        return ResponseEntity.ok(ApiResponse.success("更新成功"));
    }

    @DeleteMapping("/{employerId}/tasks/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long employerId,
                                                       @PathVariable Long taskId) {
        taskService.deleteTask(employerId, taskId);
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }

    @PostMapping("/{employerId}/tasks/{taskId}/publish")
    public ResponseEntity<ApiResponse<Void>> publishTask(@PathVariable Long employerId,
                                                        @PathVariable Long taskId) {
        taskService.publishTask(employerId, taskId);
        return ResponseEntity.ok(ApiResponse.success("发布成功，等待平台审核"));
    }

    @PostMapping("/{employerId}/tasks/{taskId}/offline")
    public ResponseEntity<ApiResponse<Void>> offlineTask(@PathVariable Long employerId,
                                                        @PathVariable Long taskId) {
        taskService.offlineTask(employerId, taskId);
        return ResponseEntity.ok(ApiResponse.success("下架成功"));
    }

    @GetMapping("/{employerId}/tasks")
    public ResponseEntity<ApiResponse<List<TaskListResponse>>> getEmployerTasks(@PathVariable Long employerId,
                                                                               @RequestParam(required = false) Integer status) {
        List<TaskListResponse> tasks = taskService.getEmployerTasks(employerId, status);
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @GetMapping("/{employerId}/orders")
    public ResponseEntity<ApiResponse<List<OrderListResponse>>> getEmployerOrders(@PathVariable Long employerId,
                                                                                 @RequestParam(required = false) Integer status) {
        List<OrderListResponse> orders = orderService.getEmployerOrders(employerId, status);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{employerId}/orders/pending-audit")
    public ResponseEntity<ApiResponse<List<OrderListResponse>>> getPendingAuditOrders(@PathVariable Long employerId) {
        List<OrderListResponse> orders = orderService.getPendingAuditOrders(employerId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PostMapping("/{employerId}/orders/audit")
    public ResponseEntity<ApiResponse<Void>> auditOrder(@PathVariable Long employerId,
                                                       @Valid @RequestBody OrderAuditRequest request) {
        orderService.auditOrder(employerId, request);
        return ResponseEntity.ok(ApiResponse.success("审核完成"));
    }

    @PostMapping("/{employerId}/orders/batch-audit")
    public ResponseEntity<ApiResponse<Void>> batchAuditOrder(@PathVariable Long employerId,
                                                            @Valid @RequestBody OrderAuditRequest request) {
        orderService.batchAuditOrder(employerId, request);
        return ResponseEntity.ok(ApiResponse.success("批量审核完成"));
    }

    @GetMapping("/{employerId}/balance")
    public ResponseEntity<ApiResponse<Object>> getBalance(@PathVariable Long employerId) {
        return ResponseEntity.ok(ApiResponse.success(fundService.getBalance(employerId)));
    }

    @GetMapping("/{employerId}/fund-statistics")
    public ResponseEntity<ApiResponse<Object>> getFundStatistics(@PathVariable Long employerId) {
        return ResponseEntity.ok(ApiResponse.success(fundService.getFundStatistics(employerId)));
    }
}