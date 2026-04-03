package com.ai.study.service;

import com.ai.study.domain.*;

import java.time.LocalDate;
import java.util.List;

public interface WordService {

    /** 获取所有词书 */
    List<WordBook> getAllBooks();

    /** 获取随机复习单词 */
    List<Word> getRandomWords(Integer bookId, int limit, Long userId);

    /** 标记单词为正确（认识） */
    void markCorrect(Long userId, Long wordId, Integer bookId);

    /** 标记单词为错误（不认识） */
    void markWrong(Long userId, Long wordId, Integer bookId);

    /** 获取错词本 */
    List<WordProgress> getWrongWords(Long userId, Integer bookId);

    /** 标记单词为已掌握 */
    void markMastered(Long userId, Long wordId);

    /** 清空某词书的错词 */
    void clearWrongWords(Long userId, Integer bookId);

    /** 获取今日学习统计 */
    DailyWordStats getTodayStats(Long userId, Integer bookId);
}
