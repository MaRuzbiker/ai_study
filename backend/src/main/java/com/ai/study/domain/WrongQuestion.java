package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WrongQuestion {
    private Long id;
    private String question;
    private String answer;
    private String subject;
    private String knowledgePoint;
    private Long userId;
    private Boolean mastered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}