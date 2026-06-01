package com.taskpush.controller;

import com.taskpush.common.PageResult;
import com.taskpush.common.Result;
import com.taskpush.common.ResultCode;
import com.taskpush.common.constant.CommonConstant;
import com.taskpush.common.util.RedisUtil;
import com.taskpush.dto.GrabTaskDTO;
import com.taskpush.dto.SubmitOrderDTO;
import com.taskpush.dto.TaskQueryDTO;
import com.taskpush.dto.WithdrawApplyDTO;
import com.taskpush.entity.UserMember;
import com.taskpush.service.ITaskInfoService;
import com.taskpush.service.ITaskOrderService;
import com.taskpush.service.IUserIncomeService;
import com.taskpush.service.IUserMemberService;
import com.taskpush.service.IUserWithdrawService;
import com.taskpush.vo.LoginVO;
import com.taskpush.vo.TaskInfoVO;
import com.taskpush.vo.TaskOrderVO;
import com.taskpush.vo.UserIncomeVO;
import com.taskpush.vo.UserMemberVO;
import com.taskpush.vo.UserWithdrawVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序C端接口控制器（无需Security认证）
 */
@RestController
@RequestMapping("/api/miniapp")
public class MiniAppController {

    @Autowired
    private IUserMemberService userMemberService;

    @Autowired
    private ITaskInfoService taskInfoService;

    @Autowired
    private ITaskOrderService taskOrderService;

    @Autowired
    private IUserIncomeService userIncomeService;

    @Autowired
    private IUserWithdrawService userWithdrawService;

    @Autowired
    private RedisUtil redisUtil;

    // ==================== 登录 ====================

    /**
     * 小程序微信登录（模拟）
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestParam String code) {
        // 模拟微信登录：用code作为openid
        UserMember member = userMemberService.registerByOpenid(code);
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(String.valueOf(member.getId()));
        loginVO.setUsername(member.getNickname());
        return Result.success(loginVO);
    }

    // ==================== 任务 ====================

    /**
     * 获取可接任务列表
     */
    @GetMapping("/tasks")
    public Result<PageResult<TaskInfoVO>> tasks(TaskQueryDTO dto,
                                                 @RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        PageResult<TaskInfoVO> pageResult = taskInfoService.pageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * 任务详情
     */
    @GetMapping("/tasks/{id}")
    public Result<TaskInfoVO> taskDetail(@PathVariable Long id) {
        TaskInfoVO vo = taskInfoService.getById(id);
        return Result.success(vo);
    }

    // ==================== 抢单 ====================

    /**
     * 抢单
     */
    @PostMapping("/grab")
    public Result<Void> grab(@RequestBody @Validated GrabTaskDTO dto,
                              @RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        taskOrderService.grabTask(dto, userId);
        return Result.success(null);
    }

    // ==================== 我的订单 ====================

    /**
     * 我的订单
     */
    @GetMapping("/my-orders")
    public Result<List<TaskOrderVO>> myOrders(@RequestParam(required = false) Integer status,
                                               @RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        List<TaskOrderVO> orders = taskOrderService.getMyOrders(userId, status);
        return Result.success(orders);
    }

    /**
     * 提交凭证
     */
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody @Validated SubmitOrderDTO dto,
                                @RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        taskOrderService.submitCert(dto, userId);
        return Result.success(null);
    }

    // ==================== 我的收益 ====================

    /**
     * 我的收益流水
     */
    @GetMapping("/my-income")
    public Result<List<UserIncomeVO>> myIncome(@RequestParam(required = false) Integer incomeType,
                                                @RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        List<UserIncomeVO> list = userIncomeService.pageQuery(userId, incomeType);
        return Result.success(list);
    }

    // ==================== 我的资料 ====================

    /**
     * 我的个人资料
     */
    @GetMapping("/my-profile")
    public Result<UserMemberVO> myProfile(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        UserMemberVO vo = userMemberService.getMyProfile(userId);
        return Result.success(vo);
    }

    // ==================== 提现 ====================

    /**
     * 提现申请
     */
    @PostMapping("/withdraw/apply")
    public Result<Void> withdrawApply(@RequestBody @Validated WithdrawApplyDTO dto,
                                       @RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        userWithdrawService.apply(dto, userId);
        return Result.success(null);
    }

    /**
     * 提现历史
     */
    @GetMapping("/withdraw/history")
    public Result<List<UserWithdrawVO>> withdrawHistory(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        List<UserWithdrawVO> list = userWithdrawService.getMyWithdrawHistory(userId);
        return Result.success(list);
    }

    // ==================== 文件上传 ====================

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<String> upload() {
        return Result.success("https://example.com/upload/sample.jpg");
    }

    // ==================== 私有方法 ====================

    /**
     * 从请求头中解析用户ID
     */
    private Long parseUserId(String token) {
        if (token == null || token.isEmpty()) {
            throw new com.taskpush.common.BusinessException(ResultCode.UNAUTHORIZED);
        }
        // 如果是Bearer格式，去掉前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String userIdStr = redisUtil.getString(CommonConstant.USER_TOKEN_PREFIX + token);
        if (userIdStr == null) {
            throw new com.taskpush.common.BusinessException(ResultCode.UNAUTHORIZED.getCode(), "未登录或登录已过期");
        }
        return Long.parseLong(userIdStr);
    }
}
