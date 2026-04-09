package com.ai.study.service;

import com.ai.study.domain.ResumeTemplate;
import java.util.List;

public interface ResumeTemplateService {
    List<ResumeTemplate> getAllTemplates();
    ResumeTemplate getTemplateById(Long id);
}
