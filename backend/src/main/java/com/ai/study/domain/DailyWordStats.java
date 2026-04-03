package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyWordStats {
    private Long id;
    private Long userId;
    private Integer bookId;
    private LocalDate statDate;
    private Integer learnedCount;
    private Integer correctCount;
    private Integer wrongCount;
}
