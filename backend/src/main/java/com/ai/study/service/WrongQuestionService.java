package com.ai.study.service;

import com.ai.study.domain.WrongQuestion;
import java.util.List;
import java.util.Map;

public interface WrongQuestionService {
    WrongQuestion add(WrongQuestion wrongQuestion);
    WrongQuestion getById(Long id);
    List<WrongQuestion> getList(String subject, Boolean mastered);
    boolean update(WrongQuestion wrongQuestion);
    boolean delete(Long id);
    String analyze(String question);
    Map<String, Object> getStats();
}