package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.UserMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface UserMemberMapper extends BaseMapper<UserMember> {

    /**
     * 根据微信 OpenID 查询用户
     *
     * @param openid 微信 OpenID
     * @return 用户实体
     */
    @Select("SELECT * FROM user_member WHERE openid = #{openid} AND is_deleted = 0")
    UserMember selectByOpenid(@Param("openid") String openid);

    /**
     * 统计某上级代理的下级用户数量
     *
     * @param parentId 上级代理ID
     * @return 下级用户数量
     */
    @Select("SELECT COUNT(*) FROM user_member WHERE parent_id = #{parentId} AND is_deleted = 0")
    int countByParentId(@Param("parentId") Long parentId);

}
