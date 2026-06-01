package com.taskpush.service;

import com.taskpush.common.PageResult;
import com.taskpush.dto.WithdrawApplyDTO;
import com.taskpush.dto.WithdrawAuditDTO;
import com.taskpush.dto.WithdrawQueryDTO;
import com.taskpush.vo.UserWithdrawVO;

import java.util.List;

/**
 * 用户提现服务接口
 */
public interface IUserWithdrawService {

    /**
     * 用户提现申请
     *
     * @param dto    提现参数
     * @param userId 用户ID
     */
    void apply(WithdrawApplyDTO dto, Long userId);

    /**
     * 分页查询提现记录
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageResult<UserWithdrawVO> pageQuery(WithdrawQueryDTO dto);

    /**
     * 审核提现
     *
     * @param dto     审核参数
     * @param auditId 审核人ID
     */
    void audit(WithdrawAuditDTO dto, Long auditId);

    /**
     * 我的提现历史
     *
     * @param userId 用户ID
     * @return 提现列表
     */
    List<UserWithdrawVO> getMyWithdrawHistory(Long userId);
}
