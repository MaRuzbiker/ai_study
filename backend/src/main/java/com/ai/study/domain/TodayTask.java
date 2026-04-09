package com.ai.study.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TodayTask {
    private Long id;
    private Long userId;
    private Long taskId;
    private String title;
    @JsonProperty("todayTargetMinutes")
    private Integer targetMinutes;       // 今日目标分钟数（前端用 todayTargetMinutes）
    private Integer accumulatedSeconds;   // 累计学习秒数
    private Boolean done;                // 是否完成
    private LocalDate studyDate;         // 学习日期
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
