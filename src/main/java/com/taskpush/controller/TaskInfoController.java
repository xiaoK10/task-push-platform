package com.taskpush.controller;

import com.taskpush.common.PageResult;
import com.taskpush.common.Result;
import com.taskpush.dto.TaskQueryDTO;
import com.taskpush.dto.TaskSaveDTO;
import com.taskpush.service.ITaskInfoService;
import com.taskpush.vo.TaskInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务信息管理控制器
 */
@RestController
@RequestMapping("/api/admin/task")
public class TaskInfoController {

    @Autowired
    private ITaskInfoService taskInfoService;

    /**
     * 分页查询任务列表
     */
    @GetMapping("/list")
    public Result<PageResult<TaskInfoVO>> list(TaskQueryDTO dto) {
        PageResult<TaskInfoVO> pageResult = taskInfoService.pageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * 任务详情
     */
    @GetMapping("/{id}")
    public Result<TaskInfoVO> getById(@PathVariable Long id) {
        TaskInfoVO vo = taskInfoService.getById(id);
        return Result.success(vo);
    }

    /**
     * 新增任务
     */
    @PostMapping("/save")
    public Result<Void> saveCreate(@RequestBody @Validated(TaskSaveDTO.Create.class) TaskSaveDTO dto) {
        taskInfoService.save(dto);
        return Result.success(null);
    }

    /**
     * 编辑任务
     */
    @PutMapping("/save")
    public Result<Void> saveUpdate(@RequestBody @Validated(TaskSaveDTO.Update.class) TaskSaveDTO dto) {
        taskInfoService.save(dto);
        return Result.success(null);
    }

    /**
     * 更新任务状态（上下架/完结）
     */
    @PutMapping("/status/{id}/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        taskInfoService.updateStatus(id, status);
        return Result.success(null);
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskInfoService.delete(id);
        return Result.success(null);
    }
}
