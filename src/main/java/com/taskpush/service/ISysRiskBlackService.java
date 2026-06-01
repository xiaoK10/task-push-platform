package com.taskpush.service;

import com.taskpush.dto.RiskBlackDTO;
import com.taskpush.vo.SysRiskBlackVO;

import java.util.List;

/**
 * 风控黑名单服务接口
 */
public interface ISysRiskBlackService {

    /**
     * 添加到黑名单（同时缓存Redis）
     *
     * @param dto 黑名单数据
     */
    void add(RiskBlackDTO dto);

    /**
     * 移除黑名单
     *
     * @param id 黑名单记录ID
     */
    void remove(Long id);

    /**
     * 校验是否在黑名单中
     *
     * @param value 校验值
     * @param type  类型(1手机号 2设备ID 3OpenID)
     * @return true-在黑名单中
     */
    boolean checkBlack(String value, Integer type);

    /**
     * 黑名单列表
     *
     * @return 黑名单VO列表
     */
    List<SysRiskBlackVO> list();
}
