package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Resume {
    private Long id;
    private Long userId;
    private Long templateId;
    private String title;
    private String content;
    private String status; // VISIBLE / DRAFT
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
