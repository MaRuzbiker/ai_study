package com.ai.study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AiTaskPlanRequest {
    @NotBlank(message = "学习目标描述不能为空")
    private String goalDescription;

    @NotNull(message = "期望任务数量不能为空")
    @Positive(message = "期望任务数量必须大于0")
    private Integer taskCount;

    private String preference;
    
    /**
     * AI 模型选择: qwen-turbo(快速便宜) / qwen-plus(均衡) / qwen-max(最强)
     * 默认使用 qwen-turbo
     */
    private String model;
}




