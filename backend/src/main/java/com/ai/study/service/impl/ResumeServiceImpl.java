package com.ai.study.service.impl;

import com.ai.study.client.DeepSeekClient;
import com.ai.study.client.QwenClient;
import com.ai.study.config.QwenProperties;
import com.ai.study.domain.Resume;
import com.ai.study.dto.ResumeGenerateRequest;
import com.ai.study.mapper.ResumeMapper;
import com.ai.study.mapper.ResumeTemplateMapper;
import com.ai.study.service.ResumeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;
    private final ResumeTemplateMapper resumeTemplateMapper;
    private final QwenClient qwenClient;
    private final DeepSeekClient deepSeekClient;
    private final QwenProperties qwenProperties;
    private final ObjectMapper objectMapper;

    @Override
    public List<Resume> getMyResumes(Long userId) {
        return resumeMapper.findByUserId(userId);
    }

    @Override
    public Resume getResumeById(Long id) {
        return resumeMapper.findById(id);
    }

    @Override
    @Transactional
    public Resume saveResume(Long userId, Resume resume) {
        if (resume.getId() != null) {
            // 更新已有简历
            Resume existing = resumeMapper.findById(resume.getId());
            if (existing == null) {
                throw new RuntimeException("简历不存在");
            }
            if (existing.getUserId() != null && !existing.getUserId().equals(userId)) {
                throw new RuntimeException("无权限修改此简历");
            }
            resume.setUpdateTime(LocalDateTime.now());
            resumeMapper.update(resume);
            return resumeMapper.findById(resume.getId());
        } else {
            // 新建简历
            resume.setUserId(userId);
            resume.setCreateTime(LocalDateTime.now());
            resume.setUpdateTime(LocalDateTime.now());
            if (resume.getStatus() == null || resume.getStatus().isEmpty()) {
                resume.setStatus("VISIBLE");
            }
            resumeMapper.insert(resume);
            return resume;
        }
    }

    @Override
    @Transactional
    public void deleteResume(Long id) {
        Resume resume = resumeMapper.findById(id);
        if (resume == null) {
            throw new RuntimeException("简历不存在");
        }
        resumeMapper.deleteById(id);
    }

    @Override
    public String generateResume(Long userId, Map<String, Object> request) {
        try {
            String prompt = buildResumePrompt(request);
            String model = request.get("model") != null ? request.get("model").toString() : null;
            String responseText = callAi(prompt, model);
            return parseAiResponse(responseText);
        } catch (Exception e) {
            log.error("AI 生成简历失败", e);
            throw new RuntimeException("AI 生成简历失败: " + e.getMessage());
        }
    }

    /**
     * 构建简历生成的 Prompt
     */
    private String buildResumePrompt(Map<String, Object> request) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一名专业的简历顾问，擅长为求职者撰写专业、突出亮点的简历。\n\n");
        sb.append("请根据以下信息生成一份完整的简历内容（JSON 格式）：\n\n");

        if (request.get("userName") != null) {
            sb.append("【个人信息】\n");
            sb.append("姓名：").append(request.get("userName")).append("\n");
            if (request.get("gender") != null) sb.append("性别：").append(request.get("gender")).append("\n");
            if (request.get("phone") != null) sb.append("电话：").append(request.get("phone")).append("\n");
            if (request.get("email") != null) sb.append("邮箱：").append(request.get("email")).append("\n");
            if (request.get("targetPosition") != null) sb.append("目标岗位：").append(request.get("targetPosition")).append("\n");
            sb.append("\n");
        }

        if (request.get("education") != null) {
            sb.append("【教育背景】\n").append(request.get("education")).append("\n\n");
        }
        if (request.get("workExperience") != null) {
            sb.append("【工作经历】\n").append(request.get("workExperience")).append("\n\n");
        }
        if (request.get("projects") != null) {
            sb.append("【项目经历】\n").append(request.get("projects")).append("\n\n");
        }
        if (request.get("skills") != null) {
            sb.append("【专业技能】\n").append(request.get("skills")).append("\n\n");
        }
        if (request.get("selfEvaluation") != null) {
            sb.append("【自我评价】\n").append(request.get("selfEvaluation")).append("\n\n");
        }
        if (request.get("additionalInfo") != null) {
            sb.append("【补充信息】\n").append(request.get("additionalInfo")).append("\n\n");
        }

        sb.append("请严格按照以下 JSON 格式输出（不要包含 markdown 代码块标记）：\n");
        sb.append("{\n");
        sb.append("  \"personal\": { \"name\":\"...\", \"gender\":\"...\", \"phone\":\"...\", \"email\":\"...\", \"targetPosition\":\"...\" },\n");
        sb.append("  \"education\": [{ \"school\":\"...\", \"degree\":\"...\", \"major\":\"...\", \"startDate\":\"...\", \"endDate\":\"...\", \"gpa\":\"...\" }],\n");
        sb.append("  \"experience\": [{ \"company\":\"...\", \"position\":\"...\", \"startDate\":\"...\", \"endDate\":\"...\", \"description\":\"...\" }],\n");
        sb.append("  \"projects\": [{ \"name\":\"...\", \"role\":\"...\", \"startDate\":\"...\", \"endDate\":\"...\", \"description\":\"...\", \"achievements\":\"...\" }],\n");
        sb.append("  \"skills\": [\"技能1\", \"技能2\", ...],\n");
        sb.append("  \"summary\": \"自我评价（3-5句话，专业且有说服力）\"\n");
        sb.append("}\n\n");
        sb.append("要求：\n");
        sb.append("1. 工作经历和项目经历的描述要使用专业动词（如\"主导\"、\"优化\"、\"实现\"、\"提升\"等）\n");
        sb.append("2. 尽可能量化成果（如\"提升性能30%\"、\"管理5人团队\"等）\n");
        sb.append("3. 内容真实专业，不要夸大其词\n");
        sb.append("4. 仅返回 JSON，不要包含任何解释说明文字\n");

        return sb.toString();
    }

    /**
     * 解析 AI 返回的 JSON 内容
     */
    private String parseAiResponse(String responseText) {
        try {
            String cleaned = responseText.trim();
            // 移除 markdown 代码块
            if (cleaned.startsWith("```json")) {
                cleaned = cleaned.substring(7).trim();
            } else if (cleaned.startsWith("```")) {
                cleaned = cleaned.substring(3).trim();
            }
            if (cleaned.endsWith("```")) {
                cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
            }
            // 提取 JSON 区间
            int jsonStart = cleaned.indexOf("{");
            int jsonEnd = cleaned.lastIndexOf("}");
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                cleaned = cleaned.substring(jsonStart, jsonEnd + 1);
            }
            // 验证 JSON 有效性
            objectMapper.readTree(cleaned);
            return cleaned;
        } catch (Exception e) {
            log.warn("AI 返回内容解析失败，原始内容: {}", responseText);
            // 返回原始内容（可能是 Markdown 格式）
            return responseText.trim();
        }
    }

    /**
     * AI 调用：根据配置自动选择
     */
    private String callAi(String prompt, String model) {
        String qwenKey = qwenProperties.getApiKey();
        if (qwenKey != null && !qwenKey.isEmpty()) {
            log.info("使用通义千问生成简历, model={}", model != null ? model : "default");
            return qwenClient.chat(prompt, model);
        }
        log.info("使用 DeepSeek 生成简历");
        return deepSeekClient.chat(prompt);
    }
}
