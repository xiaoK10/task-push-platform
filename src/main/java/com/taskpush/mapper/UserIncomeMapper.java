package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.UserIncome;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 收益流水 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface UserIncomeMapper extends BaseMapper<UserIncome> {

    /**
     * 按日期范围汇总收益金额
     *
     * @param startDate 开始日期（含）
     * @param endDate   结束日期（含）
     * @return 汇总金额
     */
    @Select("SELECT IFNULL(SUM(amount), 0) FROM user_income " +
            "WHERE is_deleted = 0 " +
            "AND create_time >= #{startDate} " +
            "AND create_time <= #{endDate}")
    BigDecimal sumIncomeByDate(@Param("startDate") String startDate,
                               @Param("endDate") String endDate);

}
