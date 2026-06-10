package com.aieppay.task.service;

import com.aieppay.task.entity.DisputeInfo;

import java.util.List;
import java.util.Map;

public interface DisputeService {

    Long createDispute(Long userId, Long orderId, Integer type, String content, String evidence);

    void processDispute(Long adminId, Long disputeId, Integer result, String resultContent);

    DisputeInfo getDisputeDetail(Long disputeId);

    List<DisputeInfo> getPendingDisputes();

    List<DisputeInfo> getUserDisputes(Long userId);

    Map<String, Object> getDisputeStatistics();
}