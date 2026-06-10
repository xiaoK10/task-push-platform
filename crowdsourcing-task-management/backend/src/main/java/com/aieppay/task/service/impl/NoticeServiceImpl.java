package com.aieppay.task.service.impl;

import com.aieppay.task.entity.SystemNotice;
import com.aieppay.task.exception.ServiceException;
import com.aieppay.task.mapper.SystemNoticeMapper;
import com.aieppay.task.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final SystemNoticeMapper systemNoticeMapper;

    @Override
    @Transactional
    public void createNotice(String title, String content, Integer type) {
        SystemNotice notice = new SystemNotice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setType(type);
        notice.setStatus(1);
        systemNoticeMapper.insert(notice);
    }

    @Override
    @Transactional
    public void updateNotice(Long noticeId, String title, String content, Integer status) {
        SystemNotice notice = systemNoticeMapper.selectById(noticeId);
        if (notice == null) {
            throw ServiceException.of("公告不存在");
        }

        if (title != null) {
            notice.setTitle(title);
        }
        if (content != null) {
            notice.setContent(content);
        }
        if (status != null) {
            notice.setStatus(status);
        }
        systemNoticeMapper.updateById(notice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId) {
        SystemNotice notice = systemNoticeMapper.selectById(noticeId);
        if (notice == null) {
            throw ServiceException.of("公告不存在");
        }
        systemNoticeMapper.deleteById(noticeId);
    }

    @Override
    public List<SystemNotice> getNotices(Integer type) {
        return systemNoticeMapper.selectByType(type);
    }

    @Override
    public SystemNotice getNoticeDetail(Long noticeId) {
        return systemNoticeMapper.selectById(noticeId);
    }
}