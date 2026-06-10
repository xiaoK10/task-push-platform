package com.aieppay.task.service;

import com.aieppay.task.dto.request.OrderAuditRequest;
import com.aieppay.task.dto.response.OrderListResponse;

import java.util.List;

public interface OrderService {

    void acceptTask(Long workerId, Long taskId);

    void submitOrder(Long workerId, Long orderId, String deliveryContent);

    void auditOrder(Long employerId, OrderAuditRequest request);

    void batchAuditOrder(Long employerId, OrderAuditRequest request);

    void retrySubmit(Long workerId, Long orderId, String deliveryContent);

    void cancelOrder(Long workerId, Long orderId);

    List<OrderListResponse> getWorkerOrders(Long workerId, Integer status);

    List<OrderListResponse> getEmployerOrders(Long employerId, Integer status);

    List<OrderListResponse> getPendingAuditOrders(Long employerId);

    OrderListResponse getOrderDetail(Long orderId);

    void autoAuditExpiredOrders();
}