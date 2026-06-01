package com.taskpush.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 任务分类实体类
 *
 * @author task-push
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_category")
public class TaskCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类名称 */
    private String categoryName;

    /** 父分类ID(0为顶级) */
    private Long parentId;

    /** 分类图标 */
    private String icon;

    /** 排序号 */
    private Integer sort;

    /** 状态(0隐藏 1显示) */
    private Integer status;

    /** 逻辑删除(0未删除 1已删除) */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
