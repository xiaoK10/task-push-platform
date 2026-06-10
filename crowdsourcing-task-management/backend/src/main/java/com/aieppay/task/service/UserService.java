package com.aieppay.task.service;

import com.aieppay.task.dto.request.RealNameAuthRequest;
import com.aieppay.task.dto.request.WxLoginRequest;
import com.aieppay.task.dto.response.UserInfoResponse;

public interface UserService {

    UserInfoResponse wxLogin(WxLoginRequest request);

    void realNameAuth(Long userId, RealNameAuthRequest request);

    UserInfoResponse getUserInfo(Long userId);

    void switchIdentity(Long userId, Integer identityType);

    void updateUserInfo(Long userId, String nickName, String avatar);

    void updateWithdrawPassword(Long userId, String oldPassword, String newPassword);

    void bindWithdrawAccount(Long userId, String bankCard, String bankName, String alipayAccount);

    void logout(Long userId);

    void deleteAccount(Long userId);
}