package com.ai.study.service.impl;

import com.ai.study.domain.LearningPlan;
import com.ai.study.mapper.LearningPlanMapper;
import com.ai.study.service.LearningPlanService;
import com.ai.study.service.AiService;
import com.ai.study.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LearningPlanServiceImpl implements LearningPlanService {
    
    private final LearningPlanMapper learningPlanMapper;
    private final AiService aiService;

    @Override
    public LearningPlan create(LearningPlan plan) {
        plan.setUserId(UserContext.getUserId());
        plan.setProgress(0);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        learningPlanMapper.insert(plan);
        return plan;
    }

    @Override
    public LearningPlan getById(Long id) {
        return learningPlanMapper.selectById(id);
    }

    @Override
    public List<LearningPlan> getList() {
        Long userId = UserContext.getUserId();
        return learningPlanMapper.selectAll(userId);
    }

    @Override
    public boolean update(LearningPlan plan) {
        plan.setUpdatedAt(LocalDateTime.now());
        return learningPlanMapper.update(plan) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return learningPlanMapper.deleteById(id) > 0;
    }

    @Override
    public LearningPlan generate(String subject, String goal, Integer days) {
        String prompt = String.format(
            "请为以下学习需求生成一个%d天的学习计划，返回JSON格式的任务列表：\n" +
            "学科：%s\n目标：%s\n\n" +
            "请返回JSON数组格式，每个任务包含day(天数)、title(任务标题)、content(任务内容)、status(状态：pending)",
            days, subject, goal
        );
        
        String aiResponse = aiService.chat(prompt);
        
        LearningPlan plan = new LearningPlan();
        plan.setTitle(subject + "学习计划");
        plan.setDescription(goal);
        plan.setUserId(UserContext.getUserId());
        plan.setProgress(0);
        plan.setStartDate(java.time.LocalDate.now());
        plan.setEndDate(java.time.LocalDate.now().plusDays(days));
        plan.setTasks(aiResponse);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        
        learningPlanMapper.insert(plan);
        return plan;
    }

    @Override
    public boolean updateProgress(Long id, Integer progress) {
        LearningPlan plan = new LearningPlan();
        plan.setId(id);
        plan.setProgress(progress);
        plan.setUpdatedAt(LocalDateTime.now());
        return learningPlanMapper.update(plan) > 0;
    }
}