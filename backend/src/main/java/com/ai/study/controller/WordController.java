package com.ai.study.controller;

import com.ai.study.common.ApiResponse;
import com.ai.study.domain.*;
import com.ai.study.service.WordService;
import com.ai.study.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    /** 获取所有词书 */
    @GetMapping("/books")
    public ApiResponse<List<WordBook>> getBooks() {
        return ApiResponse.success(wordService.getAllBooks());
    }

    /** 获取随机复习单词 */
    @GetMapping("/review")
    public ApiResponse<List<Word>> getReviewWords(
            @RequestParam Integer bookId,
            @RequestParam(defaultValue = "20") Integer limit) {
        Long userId = UserContext.getUserId();
        return ApiResponse.success(wordService.getRandomWords(bookId, limit, userId));
    }

    /** 标记单词为认识 */
    @PostMapping("/correct")
    public ApiResponse<Void> markCorrect(
            @RequestParam Long wordId,
            @RequestParam Integer bookId) {
        Long userId = UserContext.getUserId();
        wordService.markCorrect(userId, wordId, bookId);
        return ApiResponse.success(null);
    }

    /** 标记单词为不认识 */
    @PostMapping("/wrong")
    public ApiResponse<Void> markWrong(
            @RequestParam Long wordId,
            @RequestParam Integer bookId) {
        Long userId = UserContext.getUserId();
        wordService.markWrong(userId, wordId, bookId);
        return ApiResponse.success(null);
    }

    /** 获取错词本 */
    @GetMapping("/wrong-book/{bookId}")
    public ApiResponse<List<WordProgress>> getWrongWords(@PathVariable Integer bookId) {
        Long userId = UserContext.getUserId();
        return ApiResponse.success(wordService.getWrongWords(userId, bookId));
    }

    /** 从错词本移除（标记为已掌握） */
    @DeleteMapping("/wrong-book/{wordId}")
    public ApiResponse<Void> removeFromWrongBook(@PathVariable Long wordId) {
        Long userId = UserContext.getUserId();
        wordService.markMastered(userId, wordId);
        return ApiResponse.success(null);
    }

    /** 清空错词本 */
    @DeleteMapping("/wrong-book/clear/{bookId}")
    public ApiResponse<Void> clearWrongBook(@PathVariable Integer bookId) {
        Long userId = UserContext.getUserId();
        wordService.clearWrongWords(userId, bookId);
        return ApiResponse.success(null);
    }

    /** 获取今日学习统计 */
    @GetMapping("/stats")
    public ApiResponse<DailyWordStats> getTodayStats(@RequestParam Integer bookId) {
        Long userId = UserContext.getUserId();
        return ApiResponse.success(wordService.getTodayStats(userId, bookId));
    }
}
