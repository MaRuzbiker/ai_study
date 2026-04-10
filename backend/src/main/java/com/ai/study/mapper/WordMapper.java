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
    List<Word> selectWordsByIds(@Param("ids") List<Long> ids);
    List<Word> selectRandomWordsExcludeMastered(@Param("bookId") Integer bookId, @Param("limit") int limit, @Param("masteredIds") List<Long> masteredIds);
    List<Word> selectRandomWordsExcludeIds(@Param("bookId") Integer bookId, @Param("limit") int limit, @Param("excludeIds") List<Long> excludeIds);
}
