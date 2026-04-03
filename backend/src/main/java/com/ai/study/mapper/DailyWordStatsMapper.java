package com.ai.study.mapper;

import com.ai.study.domain.DailyWordStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyWordStatsMapper {
    DailyWordStats selectByUserAndBookAndDate(@Param("userId") Long userId, @Param("bookId") Integer bookId, @Param("statDate") LocalDate date);
    List<DailyWordStats> selectByUser(@Param("userId") Long userId);
    int insertOrUpdate(DailyWordStats stats);
}
