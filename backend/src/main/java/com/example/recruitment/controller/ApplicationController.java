package com.example.recruitment.controller;

import com.example.recruitment.dto.ApiResponse;
import com.example.recruitment.dto.ApplicationCreateRequest;
import com.example.recruitment.dto.ApplicationStatusUpdateRequest;
import com.example.recruitment.model.Application;
import com.example.recruitment.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final RecruitmentService recruitmentService;

    public ApplicationController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public ApiResponse<List<Application>> getAllApplications() {
        List<Application> applications = recruitmentService.getAllApplications();
        return ApiResponse.success(applications);
    }

    @GetMapping("/job/{jobId}")
    public ApiResponse<List<Application>> getApplicationsByJobId(@PathVariable Long jobId) {
        List<Application> applications = recruitmentService.getApplicationsByJobId(jobId);
        return ApiResponse.success(applications);
    }

    @GetMapping("/applicant/{applicantName}")
    public ApiResponse<List<Application>> getApplicationsByApplicant(@PathVariable String applicantName) {
        List<Application> applications = recruitmentService.getApplicationsByApplicant(applicantName);
        return ApiResponse.success(applications);
    }

    @GetMapping("/{id}")
    public ApiResponse<Application> getApplicationById(@PathVariable Long id) {
        Application application = recruitmentService.getApplicationById(id);
        if (application == null) {
            return ApiResponse.error(404, "投递记录不存在");
        }
        return ApiResponse.success(application);
    }

    @PostMapping("/job/{jobId}")
    public ApiResponse<Application> createApplication(@PathVariable Long jobId, @RequestBody ApplicationCreateRequest request) {
        request.setJobId(jobId);
        Application application = recruitmentService.createApplication(request);
        return ApiResponse.success("投递成功", application);
    }

    @PostMapping
    public ApiResponse<Application> createApplicationDirect(@RequestBody ApplicationCreateRequest request) {
        Application application = recruitmentService.createApplication(request);
        return ApiResponse.success("投递成功", application);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Application> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody ApplicationStatusUpdateRequest request) {
        Application application = recruitmentService.updateApplicationStatus(id, request.getStatus());
        if (application == null) {
            return ApiResponse.error(404, "投递记录不存在");
        }
        return ApiResponse.success("状态更新成功", application);
    }
}