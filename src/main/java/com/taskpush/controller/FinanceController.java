package com.taskpush.controller;

import com.taskpush.common.PageResult;
import com.taskpush.common.Result;
import com.taskpush.dto.FinanceQueryDTO;
import com.taskpush.dto.WithdrawAuditDTO;
import com.taskpush.dto.WithdrawQueryDTO;
import com.taskpush.service.IFinanceService;
import com.taskpush.service.IUserWithdrawService;
import com.taskpush.vo.FinanceSummaryVO;
import com.taskpush.vo.UserWithdrawVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务结算管理控制器
 */
@RestController
@RequestMapping("/api/admin/finance")
public class FinanceController {

    @Autowired
    private IFinanceService financeService;

    @Autowired
    private IUserWithdrawService userWithdrawService;

    /**
     * 财务汇总
     */
    @GetMapping("/summary")
    public Result<FinanceSummaryVO> summary(FinanceQueryDTO dto) {
        FinanceSummaryVO vo = financeService.getSummary(dto);
        return Result.success(vo);
    }

    /**
     * 提现列表（分页）
     */
    @GetMapping("/withdraw/list")
    public Result<PageResult<UserWithdrawVO>> withdrawList(WithdrawQueryDTO dto) {
        PageResult<UserWithdrawVO> pageResult = userWithdrawService.pageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * 审核提现（打款/驳回）
     */
    @PostMapping("/withdraw/audit")
    public Result<Void> withdrawAudit(@RequestBody @Validated WithdrawAuditDTO dto) {
        Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userWithdrawService.audit(dto, adminId);
        return Result.success(null);
    }

    /**
     * 导出收益Excel（骨架）
     */
    @GetMapping("/export/income")
    public Result<Void> exportIncome(FinanceQueryDTO dto) {
        financeService.exportIncomeExcel(dto);
        return Result.success(null);
    }

    /**
     * 导出提现Excel（骨架）
     */
    @GetMapping("/export/withdraw")
    public Result<Void> exportWithdraw(FinanceQueryDTO dto) {
        financeService.exportWithdrawExcel(dto);
        return Result.success(null);
    }
}
