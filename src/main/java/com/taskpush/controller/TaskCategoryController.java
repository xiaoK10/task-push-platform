package com.taskpush.controller;

import com.taskpush.common.Result;
import com.taskpush.dto.CategorySaveDTO;
import com.taskpush.service.ITaskCategoryService;
import com.taskpush.vo.TaskCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务分类管理控制器
 */
@RestController
@RequestMapping("/api/admin/category")
public class TaskCategoryController {

    @Autowired
    private ITaskCategoryService taskCategoryService;

    /**
     * 分类列表
     */
    @GetMapping("/list")
    public Result<List<TaskCategoryVO>> list() {
        List<TaskCategoryVO> list = taskCategoryService.list();
        return Result.success(list);
    }

    /**
     * 新增/编辑分类
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody CategorySaveDTO dto) {
        taskCategoryService.save(dto);
        return Result.success(null);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskCategoryService.delete(id);
        return Result.success(null);
    }
}
