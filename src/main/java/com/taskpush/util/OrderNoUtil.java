package com.taskpush.util;

import cn.hutool.core.util.IdUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单号 / 提现单号生成工具
 *
 * @author task-push
 */
public class OrderNoUtil {

    /**
     * 雪花ID生成器 (workerId=1, datacenterId=1)
     */
    private static final cn.hutool.core.lang.Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 生成订单号
     * 格式: yyyyMMdd + 雪花ID后6位
     *
     * @return 订单号字符串，例如 "20260602123456"
     */
    public static String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String snowflakeId = SNOWFLAKE.nextIdStr();
        String suffix = snowflakeId.length() > 6
                ? snowflakeId.substring(snowflakeId.length() - 6)
                : String.format("%06d", Long.parseLong(snowflakeId));
        return datePart + suffix;
    }

    /**
     * 生成提现单号
     * 格式: "W" + yyyyMMdd + 雪花ID后6位
     *
     * @return 提现单号字符串，例如 "W20260602123456"
     */
    public static String generateWithdrawNo() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String snowflakeId = SNOWFLAKE.nextIdStr();
        String suffix = snowflakeId.length() > 6
                ? snowflakeId.substring(snowflakeId.length() - 6)
                : String.format("%06d", Long.parseLong(snowflakeId));
        return "W" + datePart + suffix;
    }
}
