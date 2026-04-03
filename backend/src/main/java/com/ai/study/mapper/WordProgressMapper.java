package com.ai.study.mapper;

import com.ai.study.domain.WordProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface WordProgressMapper {
    WordProgress selectByUserAndWord(@Param("userId") Long userId, @Param("wordId") Long wordId);
    List<WordProgress> selectWrongWords(@Param("userId") Long userId, @Param("bookId") Integer bookId);
    List<WordProgress> selectByUserAndBook(@Param("userId") Long userId, @Param("bookId") Integer bookId);
    int insert(WordProgress progress);
    int updateCorrect(@Param("userId") Long userId, @Param("wordId") Long wordId);
    int updateWrong(@Param("userId") Long userId, @Param("wordId") Long wordId);
    int updateMastered(@Param("userId") Long userId, @Param("wordId") Long wordId);
    int deleteByUserAndWord(@Param("userId") Long userId, @Param("wordId") Long wordId);
    int clearWrongWords(@Param("userId") Long userId, @Param("bookId") Integer bookId);
}
