package com.example.recruitment.service;

import com.example.recruitment.dto.ApplicationCreateRequest;
import com.example.recruitment.dto.JobCreateRequest;
import com.example.recruitment.dto.StatisticsResponse;
import com.example.recruitment.model.Application;
import com.example.recruitment.model.Job;
import com.example.recruitment.model.Message;

import java.util.List;

public interface RecruitmentService {
    List<Job> getAllJobs();
    List<Job> searchJobs(String title, String city, String status);
    Job getJobById(Long id);
    com.example.recruitment.dto.JobDetailResponse getJobDetailWithApplication(Long jobId, String applicantName);
    Job createJob(String title, String company, String city, String description, String requirements);
    Job createJob(JobCreateRequest request);
    void deleteJob(Long id);
    void updateJobStatus(Long id, String status);
    List<String> getCities();
    List<String> getPositions();

    List<Application> getAllApplications();
    List<Application> getApplicationsByJobId(Long jobId);
    List<Application> getApplicationsByApplicant(String applicantName);
    Application getApplicationById(Long id);
    Application createApplication(ApplicationCreateRequest request);
    Application updateApplicationStatus(Long id, String status);

    List<Message> getMessagesByApplicationId(Long applicationId);
    Message sendMessage(Long applicationId, String senderRole, String content);
    Message sendMessage(Long applicationId, String senderRole, String senderName, String content);

    StatisticsResponse getStatistics();
}