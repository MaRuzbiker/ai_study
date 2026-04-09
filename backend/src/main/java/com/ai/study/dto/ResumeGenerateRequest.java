package com.ai.study.dto;

import lombok.Data;

@Data
public class ResumeGenerateRequest {
    private Long templateId;
    private String userName;
    private String gender;
    private String phone;
    private String email;
    private String education;      // JSON 数组
    private String workExperience; // JSON 数组
    private String skills;         // JSON 数组
    private String projects;       // JSON 数组
    private String selfEvaluation;
    private String targetPosition;
    private String additionalInfo;
    private String model;           // AI 模型选择
}
