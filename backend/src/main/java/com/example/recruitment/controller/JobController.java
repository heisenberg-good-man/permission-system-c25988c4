package com.example.recruitment.controller;

import com.example.recruitment.dto.ApiResponse;
import com.example.recruitment.dto.JobCreateRequest;
import com.example.recruitment.model.Job;
import com.example.recruitment.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final RecruitmentService recruitmentService;

    public JobController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public ApiResponse<List<Job>> getAllJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String status) {
        List<Job> jobs = recruitmentService.searchJobs(title, city, status);
        return ApiResponse.success(jobs);
    }

    @GetMapping("/{id}")
    public ApiResponse<Job> getJobById(@PathVariable Long id) {
        Job job = recruitmentService.getJobById(id);
        if (job == null) {
            return ApiResponse.error(404, "职位不存在");
        }
        return ApiResponse.success(job);
    }

    @PostMapping
    public ApiResponse<Job> createJob(@RequestBody JobCreateRequest request) {
        Job job = recruitmentService.createJob(request);
        return ApiResponse.success("职位创建成功", job);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJob(@PathVariable Long id) {
        recruitmentService.deleteJob(id);
        return ApiResponse.success("职位删除成功", null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Job> updateJobStatus(@PathVariable Long id, @RequestParam String status) {
        recruitmentService.updateJobStatus(id, status);
        Job job = recruitmentService.getJobById(id);
        return ApiResponse.success("状态更新成功", job);
    }

    @GetMapping("/cities")
    public ApiResponse<List<String>> getCities() {
        List<String> cities = recruitmentService.getCities();
        return ApiResponse.success(cities);
    }

    @GetMapping("/positions")
    public ApiResponse<List<String>> getPositions() {
        List<String> positions = recruitmentService.getPositions();
        return ApiResponse.success(positions);
    }
}