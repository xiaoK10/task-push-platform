package com.aieppay.task.mapper;

import com.aieppay.task.entity.UserBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

    @Select("SELECT * FROM user_balance WHERE user_id = #{userId}")
    UserBalance selectByUserId(@Param("userId") Long userId);
}