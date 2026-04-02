package com.ai.study.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.ai.study.domain.WrongQuestion;

@Mapper
public interface WrongQuestionMapper {
    int insert(WrongQuestion wrongQuestion);
    WrongQuestion selectById(Long id);
    java.util.List<WrongQuestion> selectAll(Long userId, String subject, Boolean mastered);
    int update(WrongQuestion wrongQuestion);
    int deleteById(Long id);
    int countAll(Long userId);
    int countMastered(Long userId);
    java.util.List<java.util.Map<String, Object>> countBySubject(Long userId);
}