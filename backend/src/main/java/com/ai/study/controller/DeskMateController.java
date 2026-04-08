package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.StudyRecord;
import com.ai.study.domain.User;
import com.ai.study.mapper.StudyRecordMapper;
import com.ai.study.mapper.StudyTaskMapper;
import com.ai.study.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/deskmate")
@RequiredArgsConstructor
public class DeskMateController {

    private final UserMapper userMapper;
    private final StudyRecordMapper studyRecordMapper;
    private final StudyTaskMapper studyTaskMapper;

    @GetMapping("/search")
    public Result<Map<String, Object>> searchByNickname(@RequestParam String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return Result.error("昵称不能为空");
        }

        User user = userMapper.findByNickname(nickname.trim());
        if (user == null) {
            return Result.error("未找到该昵称的用户");
        }

        long targetUserId = user.getId();

        // 查询该用户的今日学习记录（通过 task JOIN，兼容历史数据）
        LocalDate today = LocalDate.now();
        List<StudyRecord> todayRecords = studyRecordMapper.findByTaskUserIdAndDateRange(targetUserId, today, today);

        // 获取用户所有任务（用于任务名称映射）
        List<?> tasks = studyTaskMapper.findAllByUserId(targetUserId);

        // 汇总今日各任务学习时长
        Map<String, Long> taskSeconds = new LinkedHashMap<>();
        long totalSeconds = 0;
        for (StudyRecord r : todayRecords) {
            // 任务名称优先用 task_id，找不到时用"任务ID"替代
            String taskKey = "任务" + r.getTaskId();
            long secs = r.getDurationMinutes() * 60L;
            taskSeconds.merge(taskKey, secs, Long::sum);
            totalSeconds += secs;
        }

        // 同时从今日任务 localStorage 读取各任务累计秒数（同桌前端可能存的是 localStorage）
        // 如果后端 study_record 有数据则以它为准，没有则返回 0

        long totalMinutes = totalSeconds / 60;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userId", targetUserId);
        result.put("nickname", user.getNickname());
        result.put("totalSeconds", totalSeconds);
        result.put("totalMinutes", totalMinutes);
        result.put("taskSeconds", taskSeconds);
        result.put("recordCount", todayRecords.size());

        return Result.success(result);
    }
}
