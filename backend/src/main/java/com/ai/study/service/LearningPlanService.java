package com.ai.study.service;

import com.ai.study.domain.LearningPlan;
import java.util.List;
import java.util.Map;

public interface LearningPlanService {
    LearningPlan create(LearningPlan plan);
    LearningPlan getById(Long id);
    List<LearningPlan> getList();
    boolean update(LearningPlan plan);
    boolean delete(Long id);
    LearningPlan generate(String subject, String goal, Integer days);
    boolean updateProgress(Long id, Integer progress);
}