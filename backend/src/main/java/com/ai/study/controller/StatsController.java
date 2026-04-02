package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    
    private final StatsService statsService;

    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> stats = statsService.getOverview();
        return Result.success(stats);
    }

    @GetMapping("/chart")
    public Result<Map<String, Object>> getChartData(
            @RequestParam String type,
            @RequestParam(required = false, defaultValue = "7") Integer days) {
        Map<String, Object> data = statsService.getChartData(type, days);
        return Result.success(data);
    }

    @GetMapping("/daily")
    public Result<List<Map<String, Object>>> getDailyStats(
            @RequestParam(required = false, defaultValue = "7") Integer days) {
        List<Map<String, Object>> stats = statsService.getDailyStats(days);
        return Result.success(stats);
    }

    @GetMapping("/subject")
    public Result<Map<String, Object>> getSubjectStats() {
        Map<String, Object> stats = statsService.getSubjectStats();
        return Result.success(stats);
    }
}