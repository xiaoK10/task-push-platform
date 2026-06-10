package com.aieppay.task.service.impl;

import com.aieppay.task.entity.DisputeInfo;
import com.aieppay.task.entity.OrderInfo;
import com.aieppay.task.exception.ServiceException;
import com.aieppay.task.mapper.DisputeInfoMapper;
import com.aieppay.task.mapper.OrderInfoMapper;
import com.aieppay.task.service.DisputeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DisputeServiceImpl implements DisputeService {

    @Autowired

    private DisputeInfoMapper disputeInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    @Transactional
    public Long createDispute(Long userId, Long orderId, Integer type, String content, String evidence) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw ServiceException.of("订单不存在");
        }

        DisputeInfo dispute = new DisputeInfo();
        dispute.setOrderId(orderId);
        dispute.setPlaintiffId(userId);
        dispute.setDefendantId(type == 1 ? order.getWorkerId() : order.getEmployerId());
        dispute.setType(type);
        dispute.setContent(content);
        dispute.setEvidence(evidence);
        dispute.setStatus(0);
        disputeInfoMapper.insert(dispute);

        return dispute.getId();
    }

    @Override
    @Transactional
    public void processDispute(Long adminId, Long disputeId, Integer result, String resultContent) {
        DisputeInfo dispute = disputeInfoMapper.selectById(disputeId);
        if (dispute == null) {
            throw ServiceException.of("纠纷不存在");
        }

        dispute.setStatus(2);
        dispute.setResult(result);
        dispute.setResultContent(resultContent);
        dispute.setArbitratorId(adminId);
        dispute.setProcessedAt(LocalDateTime.now());
        disputeInfoMapper.updateById(dispute);
    }

    @Override
    public DisputeInfo getDisputeDetail(Long disputeId) {
        return disputeInfoMapper.selectById(disputeId);
    }

    @Override
    public List<DisputeInfo> getPendingDisputes() {
        return disputeInfoMapper.selectByStatus(0);
    }

    @Override
    public List<DisputeInfo> getUserDisputes(Long userId) {
        return disputeInfoMapper.selectByUserId(userId);
    }

    @Override
    public Map<String, Object> getDisputeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pendingCount", disputeInfoMapper.countByStatus(0));
        stats.put("processingCount", disputeInfoMapper.countByStatus(1));
        stats.put("resolvedCount", disputeInfoMapper.countByStatus(2));
        return stats;
    }
}