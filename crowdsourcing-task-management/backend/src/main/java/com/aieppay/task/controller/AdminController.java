package com.aieppay.task.controller;

import com.aieppay.task.dto.response.ApiResponse;
import com.aieppay.task.dto.response.TaskListResponse;
import com.aieppay.task.entity.DisputeInfo;
import com.aieppay.task.entity.UserInfo;
import com.aieppay.task.service.DisputeService;
import com.aieppay.task.service.NoticeService;
import com.aieppay.task.service.TaskService;
import com.aieppay.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;
    private final DisputeService disputeService;
    private final NoticeService noticeService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserInfo>>> getAllUsers(
            @RequestParam(required = false) Integer identityType,
            @RequestParam(required = false) Integer status) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Object>> getUserDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserInfo(userId)));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(@PathVariable Long userId,
                                                             @RequestParam Integer status) {
        return ResponseEntity.ok(ApiResponse.success("状态更新成功"));
    }

    @PutMapping("/users/{userId}/credit")
    public ResponseEntity<ApiResponse<Void>> updateCreditScore(@PathVariable Long userId,
                                                              @RequestParam Integer creditScore) {
        return ResponseEntity.ok(ApiResponse.success("信誉分更新成功"));
    }

    @GetMapping("/tasks/pending-audit")
    public ResponseEntity<ApiResponse<List<TaskListResponse>>> getPendingAuditTasks() {
        List<TaskListResponse> tasks = taskService.getPendingAuditTasks();
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @PostMapping("/tasks/{taskId}/audit")
    public ResponseEntity<ApiResponse<Void>> platformAuditTask(@PathVariable Long taskId,
                                                              @RequestParam Integer result,
                                                              @RequestParam(required = false) String rejectReason) {
        taskService.platformAuditTask(taskId, result, rejectReason);
        return ResponseEntity.ok(ApiResponse.success("审核完成"));
    }

    @GetMapping("/disputes/pending")
    public ResponseEntity<ApiResponse<List<DisputeInfo>>> getPendingDisputes() {
        List<DisputeInfo> disputes = disputeService.getPendingDisputes();
        return ResponseEntity.ok(ApiResponse.success(disputes));
    }

    @GetMapping("/disputes/{disputeId}")
    public ResponseEntity<ApiResponse<DisputeInfo>> getDisputeDetail(@PathVariable Long disputeId) {
        DisputeInfo dispute = disputeService.getDisputeDetail(disputeId);
        return ResponseEntity.ok(ApiResponse.success(dispute));
    }

    @PostMapping("/disputes/{disputeId}/process")
    public ResponseEntity<ApiResponse<Void>> processDispute(@PathVariable Long disputeId,
                                                           @RequestParam Long adminId,
                                                           @RequestParam Integer result,
                                                           @RequestBody String resultContent) {
        disputeService.processDispute(adminId, disputeId, result, resultContent);
        return ResponseEntity.ok(ApiResponse.success("仲裁完成"));
    }

    @GetMapping("/disputes/statistics")
    public ResponseEntity<ApiResponse<Object>> getDisputeStatistics() {
        return ResponseEntity.ok(ApiResponse.success(disputeService.getDisputeStatistics()));
    }

    @PostMapping("/notices")
    public ResponseEntity<ApiResponse<Void>> createNotice(@RequestParam String title,
                                                         @RequestBody String content,
                                                         @RequestParam Integer type) {
        noticeService.createNotice(title, content, type);
        return ResponseEntity.ok(ApiResponse.success("公告创建成功"));
    }

    @PutMapping("/notices/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> updateNotice(@PathVariable Long noticeId,
                                                         @RequestParam(required = false) String title,
                                                         @RequestBody(required = false) String content,
                                                         @RequestParam(required = false) Integer status) {
        noticeService.updateNotice(noticeId, title, content, status);
        return ResponseEntity.ok(ApiResponse.success("公告更新成功"));
    }

    @DeleteMapping("/notices/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(ApiResponse.success("公告删除成功"));
    }

    @GetMapping("/notices")
    public ResponseEntity<ApiResponse<Object>> getNotices(@RequestParam(required = false) Integer type) {
        return ResponseEntity.ok(ApiResponse.success(noticeService.getNotices(type)));
    }
}