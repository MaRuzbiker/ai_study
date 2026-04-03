package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WordProgress {
    private Long id;
    private Long userId;
    private Long wordId;
    private Integer bookId;
    private Integer correctCount;
    private Integer wrongCount;
    private String status; // NEW, LEARNING, MASTERED
    private LocalDateTime lastReviewTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // Joined fields (not in DB)
    private String word;
    private String phonetic;
    private String meaning;
}
