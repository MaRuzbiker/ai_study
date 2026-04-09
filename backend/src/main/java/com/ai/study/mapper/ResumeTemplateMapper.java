package com.ai.study.mapper;

import com.ai.study.domain.ResumeTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResumeTemplateMapper {
    List<ResumeTemplate> findAllActive();
    ResumeTemplate findById(@Param("id") Long id);
}
