package com.ai.study.service.impl;

import com.ai.study.domain.ResumeTemplate;
import com.ai.study.mapper.ResumeTemplateMapper;
import com.ai.study.service.ResumeTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeTemplateServiceImpl implements ResumeTemplateService {

    private final ResumeTemplateMapper resumeTemplateMapper;

    @Override
    public List<ResumeTemplate> getAllTemplates() {
        return resumeTemplateMapper.findAllActive();
    }

    @Override
    public ResumeTemplate getTemplateById(Long id) {
        return resumeTemplateMapper.findById(id);
    }
}
