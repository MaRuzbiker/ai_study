package com.ai.study.service;

import com.ai.study.domain.QAHistory;
import java.util.List;

public interface QAService {
    String ask(String question);
    List<QAHistory> getHistory(Integer limit);
    boolean deleteHistory(Long id);
}