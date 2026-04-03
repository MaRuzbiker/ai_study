package com.ai.study.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WordBook {
    private Integer id;
    private String name;
    private String category;
    private String description;
    private Integer wordCount;
    private LocalDateTime createTime;
}
