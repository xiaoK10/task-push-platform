package com.taskpush.service;

import com.taskpush.vo.UserIncomeVO;

import java.util.List;

/**
 * 用户收益流水服务接口
 */
public interface IUserIncomeService {

    /**
     * 查询用户收益流水
     *
     * @param userId     用户ID
     * @param incomeType 收益类型（可选）
     * @return 收益流水列表
     */
    List<UserIncomeVO> pageQuery(Long userId, Integer incomeType);
}
