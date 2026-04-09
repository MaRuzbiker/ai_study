package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.TodayTask;
import com.ai.study.service.TodayTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/today-tasks")
@RequiredArgsConstructor
public class TodayTaskController {

    private final TodayTaskService todayTaskService;

    /** 添加任务到今日任务 */
    @PostMapping
    public Result<TodayTask> addTodayTask(@RequestBody TodayTask task) {
        TodayTask saved = todayTaskService.addTodayTask(task, LocalDate.now());
        return Result.success(saved);
    }

    /** 更新今日任务（累计时间、完成状态） */
    @PutMapping("/{id}")
    public Result<TodayTask> updateTodayTask(@PathVariable Long id, @RequestBody TodayTask task) {
        task.setId(id);
        TodayTask updated = todayTaskService.updateTodayTask(task);
        return Result.success(updated);
    }

    /** 批量更新今日任务 */
    @PutMapping("/batch")
    public Result<Void> batchUpdateTodayTasks(@RequestBody List<TodayTask> tasks) {
        todayTaskService.batchUpdateTodayTasks(tasks);
        return Result.success(null);
    }

    /** 删除今日任务 */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTodayTask(@PathVariable Long id) {
        todayTaskService.deleteTodayTask(id);
        return Result.success(null);
    }

    /** 批量同步今日任务到数据库（退出登录前调用） */
    @PostMapping("/sync")
    public Result<Void> syncTodayTasks(@RequestBody List<TodayTask> tasks) {
        if (tasks == null || tasks.isEmpty()) return Result.success(null);
        todayTaskService.syncTodayTasks(tasks, LocalDate.now());
        return Result.success(null);
    }

    /** 获取指定日期的今日任务（登录后调用） */
    @GetMapping
    public Result<List<TodayTask>> getTodayTasks(@RequestParam(required = false) String date) {
        LocalDate studyDate = (date != null && !date.isBlank())
            ? LocalDate.parse(date)
            : LocalDate.now();
        List<TodayTask> tasks = todayTaskService.getTodayTasks(studyDate);
        return Result.success(tasks);
    }
}
