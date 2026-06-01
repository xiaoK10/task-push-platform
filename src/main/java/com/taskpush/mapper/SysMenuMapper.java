package com.taskpush.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taskpush.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单 Mapper 接口
 *
 * @author task-push
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询管理员拥有的菜单权限标识列表
     *
     * @param adminId 管理员ID
     * @return 权限标识列表
     */
    @Select("SELECT DISTINCT m.perms FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_admin a ON a.role_id = rm.role_id " +
            "WHERE a.id = #{adminId} " +
            "AND m.perms IS NOT NULL AND m.perms != '' " +
            "AND m.is_deleted = 0 AND m.status = 1")
    List<String> selectMenuPermsByUserId(@Param("adminId") Long adminId);

    /**
     * 递归查询管理员的菜单树（根据角色）
     *
     * @param adminId 管理员ID
     * @return 菜单树列表
     */
    @Select("WITH RECURSIVE menu_tree AS ( " +
            "    SELECT m.* FROM sys_menu m " +
            "    INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "    INNER JOIN sys_admin a ON a.role_id = rm.role_id " +
            "    WHERE a.id = #{adminId} AND m.parent_id = 0 " +
            "    AND m.is_deleted = 0 AND m.status = 1 " +
            "    UNION ALL " +
            "    SELECT m.* FROM sys_menu m " +
            "    INNER JOIN menu_tree mt ON m.parent_id = mt.id " +
            "    WHERE m.is_deleted = 0 AND m.status = 1 " +
            ") " +
            "SELECT * FROM menu_tree ORDER BY sort ASC")
    List<SysMenu> selectMenuTreeByAdminId(@Param("adminId") Long adminId);

}
