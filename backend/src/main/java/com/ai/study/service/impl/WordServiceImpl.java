package com.ai.study.service.impl;

import com.ai.study.domain.*;
import com.ai.study.mapper.*;
import com.ai.study.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordBookMapper wordBookMapper;
    private final WordMapper wordMapper;
    private final WordProgressMapper wordProgressMapper;
    private final DailyWordStatsMapper dailyWordStatsMapper;

    @Override
    public List<WordBook> getAllBooks() {
        return wordBookMapper.selectAll();
    }

    @Override
    public List<Word> getRandomWords(Integer bookId, int limit, Long userId) {
        // Get all progress records for this user and book to filter out mastered words
        List<WordProgress> progressList = wordProgressMapper.selectByUserAndBook(userId, bookId);
        Set<Long> masteredIds = progressList.stream()
                .filter(p -> "MASTERED".equals(p.getStatus()))
                .map(WordProgress::getWordId)
                .collect(Collectors.toSet());

        // Get all words for this book
        List<Word> allWords = wordMapper.selectByBookId(bookId);
        // Filter out mastered words
        List<Word> available = allWords.stream()
                .filter(w -> !masteredIds.contains(w.getId()))
                .collect(Collectors.toList());

        // Prioritize wrong words (wrong_count > 0, not mastered)
        Set<Long> wrongIds = progressList.stream()
                .filter(p -> p.getWrongCount() > 0 && !"MASTERED".equals(p.getStatus()))
                .map(WordProgress::getWordId)
                .collect(Collectors.toSet());

        List<Word> wrongWords = available.stream()
                .filter(w -> wrongIds.contains(w.getId()))
                .collect(Collectors.toList());

        // Shuffle and pick: prioritize wrong words, then fill rest with random
        java.util.Collections.shuffle(wrongWords);
        List<Word> otherWords = available.stream()
                .filter(w -> !wrongIds.contains(w.getId()))
                .collect(Collectors.toList());
        java.util.Collections.shuffle(otherWords);

        // Combine: wrong words first, then other words
        List<Word> result = new java.util.ArrayList<>();
        result.addAll(wrongWords);
        result.addAll(otherWords);
        return result.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markCorrect(Long userId, Long wordId, Integer bookId) {
        WordProgress existing = wordProgressMapper.selectByUserAndWord(userId, wordId);
        if (existing != null) {
            wordProgressMapper.updateCorrect(userId, wordId);
        } else {
            WordProgress p = new WordProgress();
            p.setUserId(userId);
            p.setWordId(wordId);
            p.setBookId(bookId);
            p.setCorrectCount(1);
            p.setWrongCount(0);
            p.setStatus("LEARNING");
            p.setLastReviewTime(LocalDateTime.now());
            wordProgressMapper.insert(p);
        }
        // Update daily stats
        updateDailyStats(userId, bookId, 1, 0);
    }

    @Override
    @Transactional
    public void markWrong(Long userId, Long wordId, Integer bookId) {
        WordProgress existing = wordProgressMapper.selectByUserAndWord(userId, wordId);
        if (existing != null) {
            wordProgressMapper.updateWrong(userId, wordId);
        } else {
            WordProgress p = new WordProgress();
            p.setUserId(userId);
            p.setWordId(wordId);
            p.setBookId(bookId);
            p.setCorrectCount(0);
            p.setWrongCount(1);
            p.setStatus("LEARNING");
            p.setLastReviewTime(LocalDateTime.now());
            wordProgressMapper.insert(p);
        }
        // Update daily stats
        updateDailyStats(userId, bookId, 1, 1);
    }

    @Override
    public List<WordProgress> getWrongWords(Long userId, Integer bookId) {
        return wordProgressMapper.selectWrongWords(userId, bookId);
    }

    @Override
    public void markMastered(Long userId, Long wordId) {
        wordProgressMapper.updateMastered(userId, wordId);
    }

    @Override
    public void clearWrongWords(Long userId, Integer bookId) {
        wordProgressMapper.clearWrongWords(userId, bookId);
    }

    @Override
    public DailyWordStats getTodayStats(Long userId, Integer bookId) {
        LocalDate today = LocalDate.now();
        DailyWordStats stats = dailyWordStatsMapper.selectByUserAndBookAndDate(userId, bookId, today);
        if (stats == null) {
            stats = new DailyWordStats();
            stats.setUserId(userId);
            stats.setBookId(bookId);
            stats.setStatDate(today);
            stats.setLearnedCount(0);
            stats.setCorrectCount(0);
            stats.setWrongCount(0);
        }
        return stats;
    }

    private void updateDailyStats(Long userId, Integer bookId, int learned, int wrong) {
        LocalDate today = LocalDate.now();
        DailyWordStats stats = new DailyWordStats();
        stats.setUserId(userId);
        stats.setBookId(bookId);
        stats.setStatDate(today);
        stats.setLearnedCount(learned);
        stats.setCorrectCount(learned - wrong);
        stats.setWrongCount(wrong);
        dailyWordStatsMapper.insertOrUpdate(stats);
    }
}
