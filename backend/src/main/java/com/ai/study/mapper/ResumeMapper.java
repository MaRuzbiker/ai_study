package com.ai.study.mapper;

import com.ai.study.domain.Resume;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResumeMapper {
    int insert(Resume resume);
    int update(Resume resume);
    int deleteById(@Param("id") Long id);
    Resume findById(@Param("id") Long id);
    List<Resume> findByUserId(@Param("userId") Long userId);
    long countByUserId(@Param("userId") Long userId);
}
