package com.ai.study.service;

import com.ai.study.domain.TodayTask;

import java.time.LocalDate;
import java.util.List;

public interface TodayTaskService {

    /** 添加任务到今日任务 */
    TodayTask addTodayTask(TodayTask task, LocalDate studyDate);

    /** 更新今日任务 */
    TodayTask updateTodayTask(TodayTask task);

    /** 批量更新今日任务 */
    void batchUpdateTodayTasks(List<TodayTask> tasks);

    /** 删除今日任务 */
    void deleteTodayTask(Long id);

    /** 批量同步今日任务 */
    void syncTodayTasks(List<TodayTask> tasks, LocalDate studyDate);

    /** 获取指定日期的今日任务 */
    List<TodayTask> getTodayTasks(LocalDate studyDate);
}
