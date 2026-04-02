package com.ai.study.service.impl;

import com.ai.study.domain.WrongQuestion;
import com.ai.study.mapper.WrongQuestionMapper;
import com.ai.study.service.WrongQuestionService;
import com.ai.study.service.AiService;
import com.ai.study.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WrongQuestionServiceImpl implements WrongQuestionService {
    
    private final WrongQuestionMapper wrongQuestionMapper;
    private final AiService aiService;

    @Override
    public WrongQuestion add(WrongQuestion wrongQuestion) {
        wrongQuestion.setUserId(UserContext.getUserId());
        wrongQuestion.setMastered(false);
        wrongQuestion.setCreatedAt(LocalDateTime.now());
        wrongQuestion.setUpdatedAt(LocalDateTime.now());
        wrongQuestionMapper.insert(wrongQuestion);
        return wrongQuestion;
    }

    @Override
    public WrongQuestion getById(Long id) {
        return wrongQuestionMapper.selectById(id);
    }

    @Override
    public List<WrongQuestion> getList(String subject, Boolean mastered) {
        Long userId = UserContext.getUserId();
        return wrongQuestionMapper.selectAll(userId, subject, mastered);
    }

    @Override
    public boolean update(WrongQuestion wrongQuestion) {
        wrongQuestion.setUpdatedAt(LocalDateTime.now());
        return wrongQuestionMapper.update(wrongQuestion) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return wrongQuestionMapper.deleteById(id) > 0;
    }

    @Override
    public String analyze(String question) {
        String prompt = "请分析以下错题，给出详细的解析思路和正确答案：\n\n" + question;
        return aiService.chat(prompt);
    }

    @Override
    public Map<String, Object> getStats() {
        Long userId = UserContext.getUserId();
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", wrongQuestionMapper.countAll(userId));
        stats.put("masteredCount", wrongQuestionMapper.countMastered(userId));
        stats.put("subjectStats", wrongQuestionMapper.countBySubject(userId));
        return stats;
    }
}