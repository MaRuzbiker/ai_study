package com.ai.study.service.impl;

import com.ai.study.domain.QAHistory;
import com.ai.study.mapper.QAHistoryMapper;
import com.ai.study.service.QAService;
import com.ai.study.service.AiService;
import com.ai.study.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QAServiceImpl implements QAService {
    
    private final QAHistoryMapper qaHistoryMapper;
    private final AiService aiService;

    @Override
    public String ask(String question) {
        String answer = aiService.chat(question);
        
        QAHistory history = new QAHistory();
        history.setQuestion(question);
        history.setAnswer(answer);
        history.setUserId(UserContext.getUserId());
        history.setCreatedAt(LocalDateTime.now());
        qaHistoryMapper.insert(history);
        
        return answer;
    }

    @Override
    public List<QAHistory> getHistory(Integer limit) {
        Long userId = UserContext.getUserId();
        return qaHistoryMapper.selectAll(userId, limit != null ? limit : 20);
    }

    @Override
    public boolean deleteHistory(Long id) {
        return qaHistoryMapper.deleteById(id) > 0;
    }
}