package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResumeTemplate {
    private Long id;
    private String name;
    private String category;
    private String thumbnail;
    private String content;
    private Integer isActive;
    private LocalDateTime createTime;
}
