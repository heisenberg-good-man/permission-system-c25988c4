package com.example.recruitment.controller;

import com.example.recruitment.dto.ApiResponse;
import com.example.recruitment.dto.StatisticsResponse;
import com.example.recruitment.service.RecruitmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final RecruitmentService recruitmentService;

    public StatisticsController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public ApiResponse<StatisticsResponse> getStatistics() {
        StatisticsResponse statistics = recruitmentService.getStatistics();
        return ApiResponse.success(statistics);
    }
}
