package com.aieppay.task.mapper;

import com.aieppay.task.entity.SystemNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SystemNoticeMapper extends BaseMapper<SystemNotice> {

    @Select("SELECT * FROM system_notice WHERE type = #{type} AND status = 1 ORDER BY created_at DESC")
    List<SystemNotice> selectByType(@Param("type") Integer type);
}