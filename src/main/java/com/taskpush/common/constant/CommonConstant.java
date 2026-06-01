package com.taskpush.common.constant;

/**
 * 通用常量定义
 */
public interface CommonConstant {

    /**
     * 任务分布式锁前缀
     */
    String TASK_LOCK_PREFIX = "task:lock:";

    /**
     * 任务库存缓存前缀
     */
    String TASK_STOCK_PREFIX = "task:stock:";

    /**
     * 用户登录token前缀
     */
    String USER_TOKEN_PREFIX = "user:token:";

    /**
     * 管理员登录token前缀
     */
    String ADMIN_TOKEN_PREFIX = "admin:token:";

    /**
     * 黑名单前缀
     */
    String BLACKLIST_PREFIX = "risk:black:";

    /**
     * 订单锁定小时数
     */
    int ORDER_LOCK_TIME = 24;
}
