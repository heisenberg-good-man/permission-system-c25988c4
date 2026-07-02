package com.example.recruitment.controller;

import com.example.recruitment.dto.ApiResponse;
import com.example.recruitment.dto.MessageSendRequest;
import com.example.recruitment.model.Message;
import com.example.recruitment.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final RecruitmentService recruitmentService;

    public MessageController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping("/application/{applicationId}")
    public ApiResponse<List<Message>> getMessagesByApplicationId(@PathVariable Long applicationId) {
        List<Message> messages = recruitmentService.getMessagesByApplicationId(applicationId);
        return ApiResponse.success(messages);
    }

    @PostMapping("/application/{applicationId}")
    public ApiResponse<Message> sendMessage(@PathVariable Long applicationId, @RequestBody MessageSendRequest request) {
        Message message = recruitmentService.sendMessage(
                applicationId,
                request.getSenderRole(),
                request.getSenderName(),
                request.getContent()
        );
        return ApiResponse.success("消息发送成功", message);
    }
}