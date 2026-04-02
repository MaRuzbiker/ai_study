package com.ai.study.client;

import com.ai.study.config.QwenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class QwenClient {

    private final RestTemplate restTemplate;
    private final QwenProperties properties;

    public String chat(String prompt) {
        return chat(prompt, null);
    }

    public String chat(String prompt, String model) {
        if (properties.getApiKey() == null || properties.getApiKey().isEmpty()) {
            log.error("Qwen API Key 未配置");
            return "AI 服务配置错误：API Key 未设置";
        }

        // 使用传入的模型，如果没有则使用配置的默认模型
        String useModel = (model != null && !model.isEmpty()) ? model : 
                (properties.getModel() != null ? properties.getModel() : "qwen-turbo");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + properties.getApiKey());

        // 使用 OpenAI 兼容格式
        Map<String, Object> body = new HashMap<>();
        body.put("model", useModel);
        body.put("messages", List.of(
                Map.of("role", "system", "content", "你是一个专业的学习规划 AI 教练，擅长帮助学生制定学习计划、解答学习问题、分析错题。"),
                Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // 使用 OpenAI 兼容接口
            String apiUrl = properties.getBaseUrl() != null ? properties.getBaseUrl()
                    : "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

            log.info("调用 Qwen API: {}", apiUrl);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);

            if (response == null) {
                log.warn("Qwen API 返回 null");
                return "AI 服务暂不可用，请稍后再试。";
            }

            // 检查错误
            if (response.containsKey("error")) {
                Object errorObj = response.get("error");
                if (errorObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> error = (Map<String, Object>) errorObj;
                    String errorMsg = (String) error.get("message");
                    log.error("Qwen API 返回错误: {}", errorMsg);
                    return "AI 服务错误：" + errorMsg;
                }
            }

            // 解析 OpenAI 兼容格式: choices[0].message.content
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                log.warn("Qwen API 返回的 choices 为空");
                return "AI 服务返回结果为空。";
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = message != null ? (String) message.get("content") : null;

            if (content == null) {
                log.warn("Qwen API 返回的 content 为空");
                return "AI 服务返回内容为空。";
            }

            return content;

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("调用 Qwen API HTTP 错误: {} - {}", e.getStatusCode(), e.getStatusText());
            if (e.getStatusCode().value() == 401) {
                return "API Key 无效或已过期，请检查配置。";
            }
            return "AI 服务调用失败：" + e.getStatusCode() + " - " + e.getStatusText();
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("调用 Qwen API 网络错误", e);
            return "AI 服务网络连接失败，请检查网络连接或稍后重试。";
        } catch (Exception e) {
            log.error("调用 Qwen 失败", e);
            return "AI 服务调用失败：" + e.getMessage();
        }
    }
}