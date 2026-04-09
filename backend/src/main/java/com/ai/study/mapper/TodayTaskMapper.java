package com.ai.study.mapper;

import com.ai.study.domain.TodayTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TodayTaskMapper {

    void insert(TodayTask task);

    void update(TodayTask task);

    void upsert(TodayTask task);

    TodayTask findById(@Param("id") Long id);

    List<TodayTask> findByUserIdAndDate(@Param("userId") Long userId, @Param("studyDate") LocalDate studyDate);

    void deleteById(@Param("id") Long id);

    void deleteByUserIdAndDate(@Param("userId") Long userId, @Param("studyDate") LocalDate studyDate);
}
