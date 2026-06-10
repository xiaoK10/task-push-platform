package com.aieppay.task.mapper;

import com.aieppay.task.entity.DisputeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DisputeInfoMapper extends BaseMapper<DisputeInfo> {

    @Select("SELECT * FROM dispute_info WHERE status = #{status} ORDER BY created_at DESC")
    List<DisputeInfo> selectByStatus(@Param("status") Integer status);

    @Select("SELECT * FROM dispute_info WHERE plaintiff_id = #{userId} OR defendant_id = #{userId} ORDER BY created_at DESC")
    List<DisputeInfo> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM dispute_info WHERE status = #{status}")
    Integer countByStatus(@Param("status") Integer status);
}