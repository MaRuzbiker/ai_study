package com.ai.study.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.ai.study.domain.QAHistory;

@Mapper
public interface QAHistoryMapper {
    int insert(QAHistory history);
    QAHistory selectById(Long id);
    java.util.List<QAHistory> selectAll(Long userId, Integer limit);
    int deleteById(Long id);
    int countAll(Long userId);
}