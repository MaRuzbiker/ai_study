package com.ai.study.controller;

import com.ai.study.common.ApiResponse;
import com.ai.study.dto.AiTaskPlanRequest;
import com.ai.study.dto.AiTaskPlanResponse;
import com.ai.study.service.AiService;
import com.ai.study.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI 学习教练")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    private Long getCurrentUserId() {
        return UserContext.getUserId();
    }

    @Operation(summary = "获取最近 7 天学习建议")
    @GetMapping("/suggestion/weekly")
    public ApiResponse<String> weeklySuggestion(
            @RequestParam(required = false, defaultValue = "qwen-turbo") String model) {
        return ApiResponse.success(aiService.generateWeeklySuggestion(getCurrentUserId(), model));
    }

    @Operation(summary = "根据目标生成任务计划")
    @PostMapping("/tasks/plan")
    public ApiResponse<AiTaskPlanResponse> generateTaskPlan(@RequestBody @Valid AiTaskPlanRequest request) {
        return ApiResponse.success(aiService.generateTaskPlan(getCurrentUserId(), request));
    }

    @Operation(summary = "获取可用的 AI 模型列表")
    @GetMapping("/models")
    public ApiResponse<List<Map<String, String>>> getAvailableModels() {
        return ApiResponse.success(List.of(
                Map.of("value", "qwen-turbo", "label", "千问 Turbo", "desc", "快速便宜，适合日常使用"),
                Map.of("value", "qwen-plus", "label", "千问 Plus", "desc", "均衡推荐，能力更强"),
                Map.of("value", "qwen-max", "label", "千问 Max", "desc", "最强能力，复杂任务首选")
        ));
    }

    @Operation(summary = "分析简历生成学习建议")
    @PostMapping("/resume/analyze")
    public ApiResponse<String> analyzeResume(@RequestBody Map<String, Object> request) {
        return ApiResponse.success(aiService.analyzeResume(getCurrentUserId(), request));
    }
}
