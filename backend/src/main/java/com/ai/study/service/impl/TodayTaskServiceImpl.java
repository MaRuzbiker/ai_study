package com.ai.study.service.impl;

import com.ai.study.domain.TodayTask;
import com.ai.study.mapper.TodayTaskMapper;
import com.ai.study.service.TodayTaskService;
import com.ai.study.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodayTaskServiceImpl implements TodayTaskService {

    private final TodayTaskMapper todayTaskMapper;

    @Override
    public TodayTask addTodayTask(TodayTask task, LocalDate studyDate) {
        Long userId = UserContext.getUserId();
        if (userId == null || userId <= 0) {
            throw new RuntimeException("用户未登录");
        }
        
        // 检查是否已存在
        List<TodayTask> existing = todayTaskMapper.findByUserIdAndDate(userId, studyDate);
        boolean exists = existing.stream().anyMatch(t -> t.getTaskId().equals(task.getTaskId()));
        if (exists) {
            throw new RuntimeException("该任务已在今日任务中");
        }
        
        task.setUserId(userId);
        task.setStudyDate(studyDate);
        if (task.getAccumulatedSeconds() == null) {
            task.setAccumulatedSeconds(0);
        }
        if (task.getDone() == null) {
            task.setDone(false);
        }
        todayTaskMapper.insert(task);
        return task;
    }

    @Override
    public TodayTask updateTodayTask(TodayTask task) {
        TodayTask existing = todayTaskMapper.findById(task.getId());
        if (existing == null) {
            throw new RuntimeException("今日任务不存在");
        }
        todayTaskMapper.update(task);
        return todayTaskMapper.findById(task.getId());
    }

    @Override
    public void batchUpdateTodayTasks(List<TodayTask> tasks) {
        for (TodayTask task : tasks) {
            if (task.getId() != null) {
                todayTaskMapper.update(task);
            }
        }
    }

    @Override
    public void deleteTodayTask(Long id) {
        todayTaskMapper.deleteById(id);
    }

    @Override
    public void syncTodayTasks(List<TodayTask> tasks, LocalDate studyDate) {
        Long userId = UserContext.getUserId();
        if (userId == null || userId <= 0) return;
        for (TodayTask task : tasks) {
            task.setUserId(userId);
            task.setStudyDate(studyDate);
            todayTaskMapper.upsert(task);
        }
    }

    @Override
    public List<TodayTask> getTodayTasks(LocalDate studyDate) {
        Long userId = UserContext.getUserId();
        if (userId == null || userId <= 0) return List.of();
        return todayTaskMapper.findByUserIdAndDate(userId, studyDate);
    }
}
