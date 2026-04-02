package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.QAHistory;
import com.ai.study.service.QAService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qa")
@RequiredArgsConstructor
public class QAController {
    
    private final QAService qaService;

    @PostMapping("/ask")
    public Result<String> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.trim().isEmpty()) {
            return Result.error("问题不能为空");
        }
        String answer = qaService.ask(question);
        return Result.success(answer);
    }

    @GetMapping("/history")
    public Result<List<QAHistory>> getHistory(@RequestParam(required = false, defaultValue = "20") Integer limit) {
        List<QAHistory> history = qaService.getHistory(limit);
        return Result.success(history);
    }

    @DeleteMapping("/history/{id}")
    public Result<Void> deleteHistory(@PathVariable Long id) {
        boolean success = qaService.deleteHistory(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}