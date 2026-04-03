package com.ai.study.mapper;

import com.ai.study.domain.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface WordMapper {
    List<Word> selectByBookId(@Param("bookId") Integer bookId);
    List<Word> selectRandomWords(@Param("bookId") Integer bookId, @Param("limit") int limit);
    List<Word> selectWrongWords(@Param("userId") Long userId, @Param("bookId") Integer bookId);
    Word selectById(@Param("id") Long id);
}
