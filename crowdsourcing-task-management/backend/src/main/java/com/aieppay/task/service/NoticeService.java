package com.aieppay.task.service;

import com.aieppay.task.entity.SystemNotice;

import java.util.List;

public interface NoticeService {

    void createNotice(String title, String content, Integer type);

    void updateNotice(Long noticeId, String title, String content, Integer status);

    void deleteNotice(Long noticeId);

    List<SystemNotice> getNotices(Integer type);

    SystemNotice getNoticeDetail(Long noticeId);
}