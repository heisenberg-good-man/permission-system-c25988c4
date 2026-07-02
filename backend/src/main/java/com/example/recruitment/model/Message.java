package com.example.recruitment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long applicationId;
    private String senderRole;
    private String senderName;
    private String content;
    @JsonProperty("sentAt")
    private LocalDateTime createdAt;

    public Message() {}

    public Message(Long id, Long applicationId, String senderRole, String content, LocalDateTime createdAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.senderRole = senderRole;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Message(Long id, Long applicationId, String senderRole, String senderName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.senderRole = senderRole;
        this.senderName = senderName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}