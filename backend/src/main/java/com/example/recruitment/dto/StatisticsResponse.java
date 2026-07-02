package com.example.recruitment.dto;

public class StatisticsResponse {
    private long activeJobs;
    private long todayApplications;
    private long communicatingCount;
    private long pendingApplications;

    public StatisticsResponse() {}

    public StatisticsResponse(long activeJobs, long todayApplications, long communicatingCount, long pendingApplications) {
        this.activeJobs = activeJobs;
        this.todayApplications = todayApplications;
        this.communicatingCount = communicatingCount;
        this.pendingApplications = pendingApplications;
    }

    public long getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(long activeJobs) {
        this.activeJobs = activeJobs;
    }

    public long getTodayApplications() {
        return todayApplications;
    }

    public void setTodayApplications(long todayApplications) {
        this.todayApplications = todayApplications;
    }

    public long getCommunicatingCount() {
        return communicatingCount;
    }

    public void setCommunicatingCount(long communicatingCount) {
        this.communicatingCount = communicatingCount;
    }

    public long getPendingApplications() {
        return pendingApplications;
    }

    public void setPendingApplications(long pendingApplications) {
        this.pendingApplications = pendingApplications;
    }
}
