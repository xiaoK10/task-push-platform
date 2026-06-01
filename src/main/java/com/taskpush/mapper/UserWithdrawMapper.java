package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.UserWithdraw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 提现 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface UserWithdrawMapper extends BaseMapper<UserWithdraw> {

    /**
     * 按日期范围汇总提现金额（仅统计已打款的）
     *
     * @param startDate 开始日期（含）
     * @param endDate   结束日期（含）
     * @return 汇总金额
     */
    @Select("SELECT IFNULL(SUM(amount), 0) FROM user_withdraw " +
            "WHERE is_deleted = 0 AND status = 1 " +
            "AND create_time >= #{startDate} " +
            "AND create_time <= #{endDate}")
    BigDecimal sumWithdrawByDate(@Param("startDate") String startDate,
                                 @Param("endDate") String endDate);

}
