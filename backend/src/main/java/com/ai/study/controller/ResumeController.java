package com.ai.study.controller;

import com.ai.study.common.Result;
import com.ai.study.domain.Resume;
import com.ai.study.domain.ResumeTemplate;
import com.ai.study.mapper.ResumeMapper;
import com.ai.study.service.ResumeService;
import com.ai.study.service.ResumeTemplateService;
import com.ai.study.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@Tag(name = "简历中心")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeTemplateService resumeTemplateService;
    private final ResumeService resumeService;
    private final ResumeMapper resumeMapper;

    private Long getCurrentUserId() {
        return UserContext.getUserId();
    }

    @Operation(summary = "获取模板列表")
    @GetMapping("/templates")
    public Result<List<ResumeTemplate>> getTemplates() {
        return Result.success(resumeTemplateService.getAllTemplates());
    }

    @Operation(summary = "获取单个模板")
    @GetMapping("/templates/{id}")
    public Result<ResumeTemplate> getTemplate(@PathVariable Long id) {
        ResumeTemplate template = resumeTemplateService.getTemplateById(id);
        if (template == null) {
            return Result.error("模板不存在");
        }
        return Result.success(template);
    }

    @Operation(summary = "保存简历")
    @PostMapping("/save")
    public Result<Resume> saveResume(@RequestBody Resume resume) {
        Resume saved = resumeService.saveResume(getCurrentUserId(), resume);
        return Result.success(saved);
    }

    @Operation(summary = "获取我的简历列表")
    @GetMapping("/my")
    public Result<List<Resume>> getMyResumes() {
        return Result.success(resumeService.getMyResumes(getCurrentUserId()));
    }

    @Operation(summary = "获取简历详情")
    @GetMapping("/{id}")
    public Result<Resume> getResume(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        if (resume == null) {
            return Result.error("简历不存在");
        }
        return Result.success(resume);
    }

    @Operation(summary = "删除简历")
    @DeleteMapping("/{id}")
    public Result<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return Result.success();
    }

    @Operation(summary = "AI 生成简历")
    @PostMapping("/generate")
    public Result<String> generateResume(@RequestBody Map<String, Object> request) {
        String result = resumeService.generateResume(getCurrentUserId(), request);
        return Result.success(result);
    }
}
