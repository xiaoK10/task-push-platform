package com.taskpush.dto;

import lombok.Data;

@Data
public class CategorySaveDTO {

    private Long id;

    private String categoryName;

    private Long parentId;

    private String icon;

    private Integer sort;
}
