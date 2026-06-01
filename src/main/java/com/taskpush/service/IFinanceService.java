package com.taskpush.service;

import com.taskpush.dto.FinanceQueryDTO;
import com.taskpush.vo.FinanceSummaryVO;

/**
 * 财务服务接口
 */
public interface IFinanceService {

    /**
     * 财务汇总
     *
     * @param dto 查询条件
     * @return 财务汇总VO
     */
    FinanceSummaryVO getSummary(FinanceQueryDTO dto);

    /**
     * 导出收益Excel（骨架方法）
     */
    void exportIncomeExcel(FinanceQueryDTO dto);

    /**
     * 导出提现Excel（骨架方法）
     */
    void exportWithdrawExcel(FinanceQueryDTO dto);
}
