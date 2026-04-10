package com.ai.study.service;

import com.ai.study.dto.AiTaskPlanRequest;
import com.ai.study.dto.AiTaskPlanResponse;

import java.util.Map;

public interface AiService {

    String generateWeeklySuggestion(Long userId, String model);

    AiTaskPlanResponse generateTaskPlan(Long userId, AiTaskPlanRequest request);
    
    /**
     * 与AI对话
     * @param question 问题
     * @return AI回答
     */
    String chat(String question);
    
    /**
     * 分析简历生成学习建议
     * @param userId 用户ID
     * @param request 简历信息
     * @return 学习建议
     */
    String analyzeResume(Long userId, Map<String, Object> request);
}




