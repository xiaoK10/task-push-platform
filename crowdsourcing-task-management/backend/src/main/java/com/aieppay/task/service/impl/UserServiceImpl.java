package com.aieppay.task.service.impl;

import com.aieppay.task.dto.request.RealNameAuthRequest;
import com.aieppay.task.dto.request.WxLoginRequest;
import com.aieppay.task.dto.response.UserInfoResponse;
import com.aieppay.task.entity.UserBalance;
import com.aieppay.task.entity.UserInfo;
import com.aieppay.task.entity.enums.AuthStatus;
import com.aieppay.task.entity.enums.IdentityType;
import com.aieppay.task.exception.ServiceException;
import com.aieppay.task.mapper.UserBalanceMapper;
import com.aieppay.task.mapper.UserInfoMapper;
import com.aieppay.task.service.UserService;
import com.aieppay.task.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements UserService {

    @Autowired

    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserBalanceMapper userBalanceMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInfoResponse wxLogin(WxLoginRequest request) {
        String mockOpenId = "mock_openid_" + System.currentTimeMillis();
        
        UserInfo existingUser = userInfoMapper.selectByWxOpenId(mockOpenId);
        
        if (existingUser != null) {
            if (existingUser.getStatus() == 0) {
                throw ServiceException.of(403, "账号已被封禁");
            }
            return buildUserResponse(existingUser);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setWxOpenId(mockOpenId);
        userInfo.setNickName("用户" + System.currentTimeMillis());
        userInfo.setIdentityType(IdentityType.WORKER.getCode());
        userInfo.setAuthStatus(AuthStatus.UN_AUTH.getCode());
        userInfo.setCreditScore(100);
        userInfo.setStatus(1);
        userInfo.setBlacklisted(0);
        userInfoMapper.insert(userInfo);

        UserBalance balance = new UserBalance();
        balance.setUserId(userInfo.getId());
        userBalanceMapper.insert(balance);

        return buildUserResponse(userInfo);
    }

    @Override
    @Transactional
    public void realNameAuth(Long userId, RealNameAuthRequest request) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        if (user.getAuthStatus() == AuthStatus.AUTHED.getCode()) {
            throw ServiceException.of("已完成实名认证");
        }

        user.setRealName(request.getRealName());
        user.setIdCard(maskIdCard(request.getIdCard()));
        user.setEnterpriseName(request.getEnterpriseName());
        user.setEnterpriseLicense(request.getEnterpriseLicense());
        user.setAuthStatus(AuthStatus.AUDITING.getCode());
        userInfoMapper.updateById(user);
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }
        return buildUserResponse(user);
    }

    @Override
    public void switchIdentity(Long userId, Integer identityType) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        if (identityType != IdentityType.WORKER.getCode() && 
            identityType != IdentityType.EMPLOYER.getCode()) {
            throw ServiceException.of("无效的身份类型");
        }

        user.setIdentityType(identityType);
        userInfoMapper.updateById(user);
    }

    @Override
    public void updateUserInfo(Long userId, String nickName, String avatar) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        if (nickName != null) {
            user.setNickName(nickName);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        userInfoMapper.updateById(user);
    }

    @Override
    public void updateWithdrawPassword(Long userId, String oldPassword, String newPassword) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        if (user.getWithdrawPassword() != null && 
            !passwordEncoder.matches(oldPassword, user.getWithdrawPassword())) {
            throw ServiceException.of("原密码错误");
        }

        user.setWithdrawPassword(passwordEncoder.encode(newPassword));
        userInfoMapper.updateById(user);
    }

    @Override
    public void bindWithdrawAccount(Long userId, String bankCard, String bankName, String alipayAccount) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        if (bankCard != null) {
            user.setBankCard(maskBankCard(bankCard));
        }
        if (bankName != null) {
            user.setBankName(bankName);
        }
        if (alipayAccount != null) {
            user.setAlipayAccount(alipayAccount);
        }
        userInfoMapper.updateById(user);
    }

    @Override
    public void logout(Long userId) {
    }

    @Override
    @Transactional
    public void deleteAccount(Long userId) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw ServiceException.of("用户不存在");
        }

        user.setStatus(0);
        userInfoMapper.updateById(user);
    }

    private UserInfoResponse buildUserResponse(UserInfo user) {
        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setNickName(user.getNickName());
        response.setAvatar(user.getAvatar());
        response.setIdentityType(user.getIdentityType());
        response.setAuthStatus(user.getAuthStatus());
        response.setCreditScore(user.getCreditScore());
        response.setStatus(user.getStatus());
        return response;
    }

    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 18) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(14);
    }

    private String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 16) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "**********" + bankCard.substring(bankCard.length() - 4);
    }
}