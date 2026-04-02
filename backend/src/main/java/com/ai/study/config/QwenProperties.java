package com.ai.study.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qwen")
public class QwenProperties {
    private String apiKey;      // API Key
    private String baseUrl;     // 基础URL，默认：https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation
    private String model;       // 模型名称，默认：qwen-turbo
}