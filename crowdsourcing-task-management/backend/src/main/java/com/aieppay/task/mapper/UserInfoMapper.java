package com.aieppay.task.mapper;

import com.aieppay.task.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("SELECT * FROM user_info WHERE wx_open_id = #{wxOpenId}")
    UserInfo selectByWxOpenId(@Param("wxOpenId") String wxOpenId);

    @Select("SELECT * FROM user_info WHERE phone = #{phone}")
    UserInfo selectByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM user_info WHERE status = 1 AND blacklisted = 0 ORDER BY created_at DESC")
    List<UserInfo> selectAllActiveUsers();

    @Select("SELECT * FROM user_info WHERE identity_type = #{identityType} AND status = 1")
    List<UserInfo> selectByIdentityType(@Param("identityType") Integer identityType);
}