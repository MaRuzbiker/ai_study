package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QAHistory {
    private Long id;
    private String question;
    private String answer;
    private Long userId;
    private LocalDateTime createdAt;
}