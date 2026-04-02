package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.LearningPlan;
import com.ai.study.service.LearningPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class LearningPlanController {
    
    private final LearningPlanService learningPlanService;

    @PostMapping
    public Result<LearningPlan> create(@RequestBody LearningPlan plan) {
        LearningPlan result = learningPlanService.create(plan);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<LearningPlan>> getList() {
        List<LearningPlan> list = learningPlanService.getList();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<LearningPlan> getById(@PathVariable Long id) {
        LearningPlan plan = learningPlanService.getById(id);
        return plan != null ? Result.success(plan) : Result.error("计划不存在");
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody LearningPlan plan) {
        plan.setId(id);
        boolean success = learningPlanService.update(plan);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = learningPlanService.delete(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PostMapping("/generate")
    public Result<LearningPlan> generate(@RequestBody Map<String, Object> request) {
        String subject = (String) request.get("subject");
        String goal = (String) request.get("goal");
        Integer days = request.get("days") != null ? ((Number) request.get("days")).intValue() : 7;
        
        if (subject == null || goal == null) {
            return Result.error("参数不完整");
        }
        
        LearningPlan plan = learningPlanService.generate(subject, goal, days);
        return Result.success(plan);
    }

    @PutMapping("/{id}/progress")
    public Result<Void> updateProgress(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer progress = request.get("progress");
        if (progress == null || progress < 0 || progress > 100) {
            return Result.error("进度值无效");
        }
        boolean success = learningPlanService.updateProgress(id, progress);
        return success ? Result.success() : Result.error("更新失败");
    }
}