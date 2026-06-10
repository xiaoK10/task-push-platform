package com.aieppay.task.controller;

import com.aieppay.task.dto.request.WithdrawRequest;
import com.aieppay.task.dto.response.ApiResponse;
import com.aieppay.task.dto.response.OrderListResponse;
import com.aieppay.task.dto.response.TaskListResponse;
import com.aieppay.task.service.DisputeService;
import com.aieppay.task.service.FundService;
import com.aieppay.task.service.OrderService;
import com.aieppay.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final TaskService taskService;
    private final OrderService orderService;
    private final FundService fundService;
    private final DisputeService disputeService;

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<List<TaskListResponse>>> getOnlineTasks(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String keyword) {
        List<TaskListResponse> tasks = taskService.getOnlineTasks(category, keyword);
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskListResponse>> getTaskDetail(@PathVariable Long taskId) {
        TaskListResponse task = taskService.getTaskDetail(taskId);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @PostMapping("/{workerId}/tasks/{taskId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptTask(@PathVariable Long workerId,
                                                       @PathVariable Long taskId) {
        orderService.acceptTask(workerId, taskId);
        return ResponseEntity.ok(ApiResponse.success("接单成功"));
    }

    @GetMapping("/{workerId}/orders")
    public ResponseEntity<ApiResponse<List<OrderListResponse>>> getWorkerOrders(
            @PathVariable Long workerId,
            @RequestParam(required = false) Integer status) {
        List<OrderListResponse> orders = orderService.getWorkerOrders(workerId, status);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{workerId}/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrderDetail(@PathVariable Long orderId) {
        OrderListResponse order = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping("/{workerId}/orders/{orderId}/submit")
    public ResponseEntity<ApiResponse<Void>> submitOrder(@PathVariable Long workerId,
                                                        @PathVariable Long orderId,
                                                        @RequestBody String deliveryContent) {
        orderService.submitOrder(workerId, orderId, deliveryContent);
        return ResponseEntity.ok(ApiResponse.success("提交成功，等待审核"));
    }

    @PostMapping("/{workerId}/orders/{orderId}/retry")
    public ResponseEntity<ApiResponse<Void>> retrySubmit(@PathVariable Long workerId,
                                                       @PathVariable Long orderId,
                                                       @RequestBody String deliveryContent) {
        orderService.retrySubmit(workerId, orderId, deliveryContent);
        return ResponseEntity.ok(ApiResponse.success("重新提交成功"));
    }

    @PostMapping("/{workerId}/orders/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long workerId,
                                                       @PathVariable Long orderId) {
        orderService.cancelOrder(workerId, orderId);
        return ResponseEntity.ok(ApiResponse.success("取消成功"));
    }

    @GetMapping("/{workerId}/balance")
    public ResponseEntity<ApiResponse<Object>> getBalance(@PathVariable Long workerId) {
        return ResponseEntity.ok(ApiResponse.success(fundService.getBalance(workerId)));
    }

    @PostMapping("/{workerId}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@PathVariable Long workerId,
                                                     @Valid @RequestBody WithdrawRequest request) {
        fundService.withdraw(workerId, request);
        return ResponseEntity.ok(ApiResponse.success("提现申请成功"));
    }

    @PostMapping("/{workerId}/disputes")
    public ResponseEntity<ApiResponse<Long>> createDispute(@PathVariable Long workerId,
                                                          @RequestParam Long orderId,
                                                          @RequestParam Integer type,
                                                          @RequestBody String content,
                                                          @RequestParam(required = false) String evidence) {
        Long disputeId = disputeService.createDispute(workerId, orderId, type, content, evidence);
        return ResponseEntity.ok(ApiResponse.success(disputeId));
    }

    @GetMapping("/{workerId}/disputes")
    public ResponseEntity<ApiResponse<Object>> getUserDisputes(@PathVariable Long workerId) {
        return ResponseEntity.ok(ApiResponse.success(disputeService.getUserDisputes(workerId)));
    }
}