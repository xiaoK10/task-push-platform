package com.aieppay.task.service.impl;

import com.aieppay.task.dto.request.OrderAuditRequest;
import com.aieppay.task.dto.response.OrderListResponse;
import com.aieppay.task.entity.OrderInfo;
import com.aieppay.task.entity.TaskInfo;
import com.aieppay.task.entity.UserInfo;
import com.aieppay.task.entity.enums.OrderStatus;
import com.aieppay.task.exception.ServiceException;
import com.aieppay.task.mapper.OrderInfoMapper;
import com.aieppay.task.mapper.TaskInfoMapper;
import com.aieppay.task.mapper.UserInfoMapper;
import com.aieppay.task.service.FundService;
import com.aieppay.task.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired

    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private TaskInfoMapper taskInfoMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private FundService fundService;

    @Override
    @Transactional
    public void acceptTask(Long workerId, Long taskId) {
        TaskInfo task = taskInfoMapper.selectById(taskId);
        if (task == null) {
            throw ServiceException.of("任务不存在");
        }
        if (task.getStatus() != OrderStatus.PENDING_ACCEPT.getCode()) {
            throw ServiceException.of("任务未上架");
        }

        UserInfo worker = userInfoMapper.selectById(workerId);
        if (worker == null) {
            throw ServiceException.of("用户不存在");
        }
        if (worker.getAuthStatus() != 2) {
            throw ServiceException.of("未完成实名认证");
        }

        int ongoingCount = orderInfoMapper.countOngoingOrders(workerId);
        if (ongoingCount >= 5) {
            throw ServiceException.of("同时进行的任务数量已达上限");
        }

        int acceptedCount = taskInfoMapper.countAcceptedOrders(taskId);
        if (acceptedCount >= task.getMaxWorkers()) {
            throw ServiceException.of("任务名额已满");
        }

        OrderInfo order = new OrderInfo();
        order.setTaskId(taskId);
        order.setWorkerId(workerId);
        order.setEmployerId(task.getEmployerId());
        order.setCommission(task.getCommission());
        order.setStatus(OrderStatus.ACCEPTED.getCode());
        order.setRetryCount(0);
        orderInfoMapper.insert(order);
    }

    @Override
    @Transactional
    public void submitOrder(Long workerId, Long orderId, String deliveryContent) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw ServiceException.of("订单不存在");
        }
        if (!order.getWorkerId().equals(workerId)) {
            throw ServiceException.of(403, "无权限操作此订单");
        }

        order.setDeliveryContent(deliveryContent);
        order.setDeliveryTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_AUDIT.getCode());
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional
    public void auditOrder(Long employerId, OrderAuditRequest request) {
        for (Long orderId : request.getOrderIds()) {
            OrderInfo order = orderInfoMapper.selectById(orderId);
            if (order == null) {
                continue;
            }
            if (!order.getEmployerId().equals(employerId)) {
                throw ServiceException.of(403, "无权限审核此订单");
            }

            processOrderAudit(order, request.getResult(), request.getRejectReason());
        }
    }

    @Override
    @Transactional
    public void batchAuditOrder(Long employerId, OrderAuditRequest request) {
        auditOrder(employerId, request);
    }

    @Override
    @Transactional
    public void retrySubmit(Long workerId, Long orderId, String deliveryContent) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw ServiceException.of("订单不存在");
        }
        if (!order.getWorkerId().equals(workerId)) {
            throw ServiceException.of(403, "无权限操作此订单");
        }
        if (order.getRetryCount() >= 2) {
            throw ServiceException.of("已达最大重试次数");
        }

        order.setDeliveryContent(deliveryContent);
        order.setDeliveryTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_AUDIT.getCode());
        order.setRetryCount(order.getRetryCount() + 1);
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long workerId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw ServiceException.of("订单不存在");
        }
        if (!order.getWorkerId().equals(workerId)) {
            throw ServiceException.of(403, "无权限操作此订单");
        }

        order.setStatus(OrderStatus.CANCELLED.getCode());
        orderInfoMapper.updateById(order);
    }

    @Override
    public List<OrderListResponse> getWorkerOrders(Long workerId, Integer status) {
        List<OrderInfo> orders = orderInfoMapper.selectByWorkerId(workerId);
        return convertToListResponse(orders);
    }

    @Override
    public List<OrderListResponse> getEmployerOrders(Long employerId, Integer status) {
        List<OrderInfo> orders = orderInfoMapper.selectByEmployerId(employerId);
        return convertToListResponse(orders);
    }

    @Override
    public List<OrderListResponse> getPendingAuditOrders(Long employerId) {
        List<OrderInfo> orders = orderInfoMapper.selectPendingAuditByEmployer(employerId);
        return convertToListResponse(orders);
    }

    @Override
    public OrderListResponse getOrderDetail(Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw ServiceException.of("订单不存在");
        }
        return convertToResponse(order);
    }

    @Override
    @Transactional
    public void autoAuditExpiredOrders() {
        List<OrderInfo> pendingOrders = orderInfoMapper.selectByStatus(OrderStatus.PENDING_AUDIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        
        for (OrderInfo order : pendingOrders) {
            TaskInfo task = taskInfoMapper.selectById(order.getTaskId());
            if (task != null && order.getDeliveryTime() != null) {
                int autoAuditHours = task.getAutoAuditHours() != null ? task.getAutoAuditHours() : 24;
                if (order.getDeliveryTime().plusHours(autoAuditHours).isBefore(now)) {
                    processOrderAudit(order, 1, "超时自动审核通过");
                }
            }
        }
    }

    private void processOrderAudit(OrderInfo order, Integer result, String rejectReason) {
        order.setAuditTime(LocalDateTime.now());
        order.setAuditUserId(order.getEmployerId());

        if (result == 1) {
            order.setStatus(OrderStatus.AUDIT_PASS.getCode());
            fundService.payoutToWorker(order.getWorkerId(), order.getCommission(), order.getId());
        } else {
            order.setStatus(OrderStatus.REJECTED.getCode());
            order.setRejectReason(rejectReason);
        }
        orderInfoMapper.updateById(order);
    }

    private List<OrderListResponse> convertToListResponse(List<OrderInfo> orders) {
        List<OrderListResponse> response = new ArrayList<>();
        for (OrderInfo order : orders) {
            response.add(convertToResponse(order));
        }
        return response;
    }

    private OrderListResponse convertToResponse(OrderInfo order) {
        OrderListResponse response = new OrderListResponse();
        response.setId(order.getId());
        response.setTaskId(order.getTaskId());
        
        TaskInfo task = taskInfoMapper.selectById(order.getTaskId());
        if (task != null) {
            response.setTaskTitle(task.getTitle());
        }
        
        UserInfo worker = userInfoMapper.selectById(order.getWorkerId());
        if (worker != null) {
            response.setWorkerNickName(worker.getNickName());
        }
        
        response.setWorkerId(order.getWorkerId());
        response.setCommission(order.getCommission());
        response.setStatus(order.getStatus());
        response.setDeliveryContent(order.getDeliveryContent());
        response.setDeliveryTime(order.getDeliveryTime());
        response.setAuditTime(order.getAuditTime());
        response.setRejectReason(order.getRejectReason());
        response.setRetryCount(order.getRetryCount());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}