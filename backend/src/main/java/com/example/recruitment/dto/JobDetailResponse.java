package com.example.recruitment.dto;

import com.example.recruitment.model.Application;
import com.example.recruitment.model.Job;

public class JobDetailResponse {
    private Job job;
    private Application myApplication;

    public JobDetailResponse() {}

    public JobDetailResponse(Job job, Application myApplication) {
        this.job = job;
        this.myApplication = myApplication;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Application getMyApplication() {
        return myApplication;
    }

    public void setMyApplication(Application myApplication) {
        this.myApplication = myApplication;
    }
}
