package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LearningPlan {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String tasks; // JSON格式存储任务列表
    private Integer progress;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}