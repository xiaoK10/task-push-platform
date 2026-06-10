package com.aieppay.task.controller;

import com.aieppay.task.dto.request.RealNameAuthRequest;
import com.aieppay.task.dto.request.WxLoginRequest;
import com.aieppay.task.dto.response.ApiResponse;
import com.aieppay.task.dto.response.UserInfoResponse;
import com.aieppay.task.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserInfoResponse>> wxLogin(@Valid @RequestBody WxLoginRequest request) {
        UserInfoResponse response = userService.wxLogin(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{userId}/auth")
    public ResponseEntity<ApiResponse<Void>> realNameAuth(@PathVariable Long userId, 
                                                          @Valid @RequestBody RealNameAuthRequest request) {
        userService.realNameAuth(userId, request);
        return ResponseEntity.ok(ApiResponse.successMessage("提交成功，请等待审核"));
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@PathVariable Long userId) {
        UserInfoResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{userId}/identity")
    public ResponseEntity<ApiResponse<Void>> switchIdentity(@PathVariable Long userId, 
                                                            @RequestParam Integer identityType) {
        userService.switchIdentity(userId, identityType);
        return ResponseEntity.ok(ApiResponse.successMessage("身份切换成功"));
    }

    @PutMapping("/{userId}/info")
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(@PathVariable Long userId,
                                                           @RequestParam(required = false) String nickName,
                                                           @RequestParam(required = false) String avatar) {
        userService.updateUserInfo(userId, nickName, avatar);
        return ResponseEntity.ok(ApiResponse.successMessage("更新成功"));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable Long userId,
                                                           @RequestParam String oldPassword,
                                                           @RequestParam String newPassword) {
        userService.updateWithdrawPassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.successMessage("密码修改成功"));
    }

    @PutMapping("/{userId}/bind-account")
    public ResponseEntity<ApiResponse<Void>> bindAccount(@PathVariable Long userId,
                                                        @RequestParam(required = false) String bankCard,
                                                        @RequestParam(required = false) String bankName,
                                                        @RequestParam(required = false) String alipayAccount) {
        userService.bindWithdrawAccount(userId, bankCard, bankName, alipayAccount);
        return ResponseEntity.ok(ApiResponse.successMessage("绑定成功"));
    }

    @PostMapping("/{userId}/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@PathVariable Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok(ApiResponse.successMessage("退出成功"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long userId) {
        userService.deleteAccount(userId);
        return ResponseEntity.ok(ApiResponse.successMessage("注销成功"));
    }
}