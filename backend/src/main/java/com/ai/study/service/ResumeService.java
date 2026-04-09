package com.ai.study.service;

import com.ai.study.domain.Resume;
import java.util.List;
import java.util.Map;

public interface ResumeService {
    List<Resume> getMyResumes(Long userId);
    Resume getResumeById(Long id);
    Resume saveResume(Long userId, Resume resume);
    void deleteResume(Long id);
    String generateResume(Long userId, Map<String, Object> request);
}
