package com.taskpush.vo;

import lombok.Data;

@Data
public class TaskCategoryVO {

    private Long id;

    private String categoryName;

    private Long parentId;

    private String icon;

    private Integer sort;

    private Integer status;

    /**
     * 任务数量
     */
    private int taskCount;
}
