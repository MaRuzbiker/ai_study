package com.ai.study.service;

import java.util.List;
import java.util.Map;

public interface StatsService {
    Map<String, Object> getOverview();
    Map<String, Object> getChartData(String type, Integer days);
    List<Map<String, Object>> getDailyStats(Integer days);
    Map<String, Object> getSubjectStats();
}