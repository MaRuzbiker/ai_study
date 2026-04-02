package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.WrongQuestion;
import com.ai.study.service.WrongQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wrong")
@RequiredArgsConstructor
public class WrongQuestionController {
    
    private final WrongQuestionService wrongQuestionService;

    @PostMapping
    public Result<WrongQuestion> add(@RequestBody WrongQuestion wrongQuestion) {
        WrongQuestion result = wrongQuestionService.add(wrongQuestion);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<WrongQuestion>> getList(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Boolean mastered) {
        List<WrongQuestion> list = wrongQuestionService.getList(subject, mastered);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<WrongQuestion> getById(@PathVariable Long id) {
        WrongQuestion wrongQuestion = wrongQuestionService.getById(id);
        return wrongQuestion != null ? Result.success(wrongQuestion) : Result.error("错题不存在");
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WrongQuestion wrongQuestion) {
        wrongQuestion.setId(id);
        boolean success = wrongQuestionService.update(wrongQuestion);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = wrongQuestionService.delete(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PostMapping("/analyze")
    public Result<String> analyze(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.trim().isEmpty()) {
            return Result.error("题目不能为空");
        }
        String analysis = wrongQuestionService.analyze(question);
        return Result.success(analysis);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = wrongQuestionService.getStats();
        return Result.success(stats);
    }
}