package com.ai.study.service.impl;

import com.ai.study.client.DeepSeekClient;
import com.ai.study.client.QwenClient;
import com.ai.study.config.QwenProperties;
import com.ai.study.domain.StudyRecord;
import com.ai.study.dto.AiTaskPlanRequest;
import com.ai.study.dto.AiTaskPlanResponse;
import com.ai.study.service.AiService;
import com.ai.study.service.StudyRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

    private final DeepSeekClient deepSeekClient;
    private final QwenClient qwenClient;
    private final QwenProperties qwenProperties;
    private final StudyRecordService studyRecordService;
    private final ObjectMapper objectMapper;

    /**
     * 根据配置自动选择 AI 客户端
     * model 参数可覆盖默认配置
     */
    private String callAi(String prompt, String model) {
        String qwenKey = qwenProperties.getApiKey();
        log.debug("Qwen API Key 配置: {}", qwenKey != null ? (qwenKey.length() > 10 ? qwenKey.substring(0, 10) + "..." : "已配置") : "未配置");
        
        // 优先使用千问（如果配置了 API Key）
        if (qwenKey != null && !qwenKey.isEmpty()) {
            log.info("使用通义千问 AI, model={}", model);
            return qwenClient.chat(prompt, model);
        }
        
        // 千问未配置，尝试 DeepSeek
        log.info("千问未配置，尝试使用 DeepSeek AI");
        return deepSeekClient.chat(prompt);
    }

    private String callAi(String prompt) {
        return callAi(prompt, null);
    }

    @Override
    public String generateWeeklySuggestion(Long userId, String model) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);

        List<StudyRecord> records = studyRecordService.listByUserAndDateRange(userId, start, end);

        String dataset = records.isEmpty()
                ? "最近 7 天没有任何学习记录。"
                : records.stream()
                .<String>map(r -> String.format("日期:%s, 学习时长:%d 秒, 学习内容或备注:%s",
                        r.getStudyDate(),
                        r.getDurationSeconds() != null ? r.getDurationSeconds() : 0,
                        r.getComment() == null ? "" : r.getComment()))
                .collect(Collectors.joining("\n"));

        String prompt = """
                你是一名学习规划与时间管理教练。\n\
                - 根据下面最近 7 天的学习记录，分析学员的总体学习时间投入、节奏和稳定性；\n\
                - 结合「今日任务完成情况」和「全部学习任务总体完成度」这两个维度，给出具体可执行的优化建议，\
                  包括今天之后 3~5 天应该如何安排学习任务；\n\
                - 输出结构建议包含：本周学习情况总结、当前任务完成度分析、下一步行动建议三个小节。\n\
                请用中文回答，语气鼓励、具体，不要出现"打卡"两个字。\n\
                最近 7 天的学习记录如下：
                %s
                """.formatted(dataset);

        return callAi(prompt, model);
    }

    @Override
    public AiTaskPlanResponse generateTaskPlan(Long userId, AiTaskPlanRequest request) {
        String prompt = """
                目标描述：%s
                期望任务数量：%d
                个性偏好/擅长方向：%s

                请以 JSON 形式严格返回以下结构：
                {
                  "summary": "对整体学习计划的简短总结",
                  "tasks": [
                    {
                      "title": "任务标题",
                      "description": "任务说明，含具体行动",
                      "suggestedHours": 3
                    }
                  ]
                }
                """.formatted(request.getGoalDescription(),
                request.getTaskCount(),
                request.getPreference() == null ? "未提供" : request.getPreference());

        String responseText = callAi(prompt, request.getModel());
        try {
            String cleanedText = responseText.trim();
            if (cleanedText.startsWith("```json")) {
                cleanedText = cleanedText.substring(7).trim();
            } else if (cleanedText.startsWith("```")) {
                cleanedText = cleanedText.substring(3).trim();
            }
            if (cleanedText.endsWith("```")) {
                cleanedText = cleanedText.substring(0, cleanedText.length() - 3).trim();
            }
            int jsonStart = cleanedText.indexOf("{");
            int jsonEnd = cleanedText.lastIndexOf("}");
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                cleanedText = cleanedText.substring(jsonStart, jsonEnd + 1);
            }
            log.debug("清理后的 JSON 文本: {}", cleanedText);
            AiTaskPlanResponse result = objectMapper.readValue(cleanedText, AiTaskPlanResponse.class);
            if (result.getTasks() == null || result.getTasks().isEmpty()) {
                log.warn("AI 返回的任务列表为空");
                result.setTasks(Collections.emptyList());
            } else {
                log.info("成功解析 {} 个任务", result.getTasks().size());
            }
            return result;
        } catch (Exception e) {
            log.error("解析 AI 任务计划失败，原始响应: {}", responseText, e);
            AiTaskPlanResponse fallback = new AiTaskPlanResponse();
            fallback.setSummary("AI 返回内容无法解析为任务计划。\n原始响应：\n" + responseText);
            fallback.setTasks(Collections.emptyList());
            return fallback;
        }
    }
    
    @Override
    public String chat(String question) {
        return callAi(question);
    }
    
    @Override
    public String analyzeResume(Long userId, Map<String, Object> request) {
        // 构建简历信息
        StringBuilder resumeInfo = new StringBuilder();
        
        // 提取手动填写的信息
        if (request.containsKey("name")) {
            resumeInfo.append("姓名: ").append(request.get("name")).append("\n");
        }
        if (request.containsKey("position")) {
            resumeInfo.append("目标职位: ").append(request.get("position")).append("\n");
        }
        if (request.containsKey("workYears")) {
            resumeInfo.append("工作年限: ").append(request.get("workYears")).append("\n");
        }
        if (request.containsKey("skills")) {
            resumeInfo.append("技能清单: ").append(request.get("skills")).append("\n");
        }
        if (request.containsKey("experience")) {
            resumeInfo.append("工作经历: ").append(request.get("experience")).append("\n");
        }
        
        // 如果有文件上传的信息
        if (request.containsKey("file")) {
            resumeInfo.append("已上传简历文件\n");
        }
        
        String prompt = """
                你是一名专业的技术学习规划顾问。请直接分析以下简历信息，不要询问任何问题，直接提供详细的学习建议。
                
                简历信息：
                %s
                
                请按照以下结构直接输出分析结果：
                1. 技术背景评估：分析求职者的技术栈和知识结构
                2. 核心优势：列出技术方面的强项
                3. 知识短板：指出需要补充的技术知识点
                4. 学习建议：针对短板提供具体的知识点学习计划，包括：
                   - 需要学习的具体技术知识点
                   - 推荐的学习资源（官方文档、教程、书籍）
                   - 学习顺序和优先级
                   - 实践项目建议
                5. 技能提升路径：提供3-6个月的技术技能提升规划
                
                请用中文回答，重点关注技术知识点的学习建议，给出具体、可执行的学习方案。不要包含任何询问或确认信息。
                """.formatted(resumeInfo.toString());
        
        return callAi(prompt);
    }
}