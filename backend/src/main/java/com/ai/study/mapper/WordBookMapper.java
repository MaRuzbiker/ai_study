package com.ai.study.mapper;

import com.ai.study.domain.WordBook;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WordBookMapper {
    List<WordBook> selectAll();
    WordBook selectById(Integer id);
    int updateWordCount(Integer id, Integer count);
}
