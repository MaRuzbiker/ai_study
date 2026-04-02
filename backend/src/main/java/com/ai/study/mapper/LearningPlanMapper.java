package com.ai.study.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.ai.study.domain.LearningPlan;

@Mapper
public interface LearningPlanMapper {
    int insert(LearningPlan plan);
    LearningPlan selectById(Long id);
    java.util.List<LearningPlan> selectAll(Long userId);
    int update(LearningPlan plan);
    int deleteById(Long id);
    int countAll(Long userId);
}