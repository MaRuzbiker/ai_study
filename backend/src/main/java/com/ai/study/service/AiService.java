package com.ai.study.service;

import com.ai.study.dto.AiTaskPlanRequest;
import com.ai.study.dto.AiTaskPlanResponse;

public interface AiService {

    String generateWeeklySuggestion(Long userId, String model);

    AiTaskPlanResponse generateTaskPlan(Long userId, AiTaskPlanRequest request);
    
    /**
     * 与AI对话
     * @param question 问题
     * @return AI回答
     */
    String chat(String question);
}




