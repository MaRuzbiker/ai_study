package com.ai.study.service.impl;

import com.ai.study.domain.StudyRecord;
import com.ai.study.domain.StudyTask;
import com.ai.study.mapper.LearningPlanMapper;
import com.ai.study.mapper.QAHistoryMapper;
import com.ai.study.mapper.StudyRecordMapper;
import com.ai.study.mapper.StudyTaskMapper;
import com.ai.study.mapper.WrongQuestionMapper;
import com.ai.study.service.StatsService;
import com.ai.study.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    
    private final WrongQuestionMapper wrongQuestionMapper;
    private final LearningPlanMapper learningPlanMapper;
    private final QAHistoryMapper qaHistoryMapper;
    private final StudyRecordMapper studyRecordMapper;
    private final StudyTaskMapper studyTaskMapper;

    @Override
    public Map<String, Object> getOverview() {
        Long userId = UserContext.getUserId();
        Map<String, Object> overview = new HashMap<>();
        
        if (userId == null || userId <= 0) {
            overview.put("wrongCount", 0);
            overview.put("masteredCount", 0);
            overview.put("masteredRate", 0);
            overview.put("qaCount", 0);
            overview.put("planCount", 0);
            return overview;
        }

        Integer totalWrong = wrongQuestionMapper.countAll(userId);
        Integer masteredCount = wrongQuestionMapper.countMastered(userId);
        Integer qaCount = qaHistoryMapper.countAll(userId);
        Integer planCount = learningPlanMapper.countAll(userId);
        
        overview.put("wrongCount", totalWrong);
        overview.put("masteredCount", masteredCount);
        overview.put("masteredRate", totalWrong > 0 ? (masteredCount * 100.0 / totalWrong) : 0);
        overview.put("qaCount", qaCount);
        overview.put("planCount", planCount);
        
        return overview;
    }

    @Override
    public Map<String, Object> getChartData(String type, Integer days) {
        return switch (type) {
            case "qa_trend" -> getQATrend(days);
            case "progress" -> getProgressData();
            default -> Collections.emptyMap();
        };
    }

    @Override
    public List<Map<String, Object>> getDailyStats(Integer days) {
        List<Map<String, Object>> result = new ArrayList<>();
        Random random = new Random();
        for (int i = days; i >= 0; i--) {
            Map<String, Object> dayStat = new HashMap<>();
            dayStat.put("date", java.time.LocalDate.now().minusDays(i).toString());
            dayStat.put("qaCount", random.nextInt(20) + 1);
            dayStat.put("wrongAdded", random.nextInt(5));
            dayStat.put("wrongSolved", random.nextInt(3));
            result.add(dayStat);
        }
        return result;
    }

    @Override
    public Map<String, Object> getSubjectStats() {
        Long userId = UserContext.getUserId();
        if (userId == null || userId <= 0) {
            return Map.of("subjectStats", List.of());
        }
        return Map.of("subjectStats", wrongQuestionMapper.countBySubject(userId));
    }

    @Override
    public Map<String, Object> getTodayStats() {
        Long userId = UserContext.getUserId();
        Map<String, Object> result = new LinkedHashMap<>();

        if (userId == null || userId <= 0) {
            result.put("totalSeconds", 0);
            result.put("totalMinutes", 0);
            result.put("taskCount", 0);
            result.put("doneTaskCount", 0);
            result.put("undoneTaskCount", 0);
            return result;
        }

        // 查询今日学习记录（仅当天，study_record.user_id）
        LocalDate today = LocalDate.now();
        List<StudyRecord> todayRecords = studyRecordMapper.findByUserIdAndDateRange(userId, today, today);

        // 今日总秒数
        long totalSeconds = todayRecords.stream().mapToLong(r -> r.getDurationSeconds() != null ? r.getDurationSeconds() : 0).sum();

        // 任务统计（来自 study_task 表）
        List<StudyTask> allTasks = studyTaskMapper.findAllByUserId(userId);
        int doneCount = (int) allTasks.stream()
            .filter(t -> "DONE".equals(t.getStatus()) || "COMPLETED".equals(t.getStatus()))
            .count();
        int undoneCount = allTasks.size() - doneCount;

        result.put("totalSeconds", totalSeconds);
        result.put("totalMinutes", totalSeconds / 60);
        result.put("taskCount", allTasks.size());
        result.put("doneTaskCount", doneCount);
        result.put("undoneTaskCount", undoneCount);

        return result;
    }
    
    private Map<String, Object> getQATrend(Integer days) {
        List<String> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();
        Random random = new Random();
        
        for (int i = days - 1; i >= 0; i--) {
            xData.add(java.time.LocalDate.now().minusDays(i).toString());
            yData.add(random.nextInt(20) + 1);
        }
        
        return Map.of("xData", xData, "yData", yData);
    }
    
    private Map<String, Object> getProgressData() {
        return Map.of(
            "plans", List.of("数学", "英语", "物理", "化学"),
            "progress", List.of(80, 65, 90, 70)
        );
    }
}