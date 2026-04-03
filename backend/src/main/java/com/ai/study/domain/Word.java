package com.ai.study.domain;

import lombok.Data;

@Data
public class Word {
    private Long id;
    private Integer bookId;
    private String word;
    private String phonetic;
    private String meaning;
    private Integer sortOrder;
}
