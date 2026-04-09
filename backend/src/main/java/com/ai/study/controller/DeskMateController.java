package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.TodayTask;
import com.ai.study.domain.User;
import com.ai.study.mapper.TodayTaskMapper;
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
    private final TodayTaskMapper todayTaskMapper;

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
        LocalDate today = LocalDate.now();

        // 直接从今日任务表查实时数据（含标题和累计秒数）
        List<TodayTask> tasks = todayTaskMapper.findByUserIdAndDate(targetUserId, today);

        // 汇总：按任务标题分组
        Map<String, Long> taskSeconds = new LinkedHashMap<>();
        long totalSeconds = 0;
        for (TodayTask t : tasks) {
            String name = (t.getTitle() != null && !t.getTitle().isBlank())
                    ? t.getTitle()
                    : "任务" + t.getTaskId();
            long secs = t.getAccumulatedSeconds() != null ? t.getAccumulatedSeconds() : 0;
            taskSeconds.merge(name, secs, Long::sum);
            totalSeconds += secs;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userId", targetUserId);
        result.put("nickname", user.getNickname());
        result.put("totalSeconds", totalSeconds);
        result.put("totalMinutes", totalSeconds / 60);
        result.put("taskSeconds", taskSeconds);
        result.put("recordCount", tasks.size());

        return Result.success(result);
    }
}
