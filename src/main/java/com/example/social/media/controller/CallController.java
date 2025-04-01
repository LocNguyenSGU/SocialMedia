package com.example.social.media.controller;

import com.example.social.media.payload.request.MessageDTO.VideoCallRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CallController {
    private final SimpMessagingTemplate messagingTemplate;

    public CallController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Gửi thông báo cuộc gọi đến người nhận
    @MessageMapping("/start-call")
    public void startCall(VideoCallRequest request) {
        // Gửi thông báo cuộc gọi đến người nhận (roomID là thông tin về phòng)
        messagingTemplate.convertAndSend("/topic/call/" + request.getReceiverId(), request);
    }
}
