package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.SysRiskBlack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 风控黑名单 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface SysRiskBlackMapper extends BaseMapper<SysRiskBlack> {

    /**
     * 检查黑名单记录是否存在
     *
     * @param blackType  黑名单类型（1手机号 2设备ID 3OpenID）
     * @param blackValue 黑名单值
     * @return 存在数量（0表示不存在）
     */
    @Select("SELECT COUNT(*) FROM sys_risk_black " +
            "WHERE black_type = #{blackType} AND black_value = #{blackValue} " +
            "AND status = 1")
    int existsByBlackValue(@Param("blackType") Integer blackType,
                           @Param("blackValue") String blackValue);

}
