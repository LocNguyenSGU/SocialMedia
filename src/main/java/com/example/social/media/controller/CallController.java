package com.example.social.media.controller;

import com.example.social.media.payload.request.MessageDTO.CallRequest;
import com.example.social.media.payload.request.MessageDTO.VideoCallRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    @MessageMapping("/call/{receiverId}")
    public void sendCallNotification(@DestinationVariable String receiverId, CallRequest callRequest) {
        System.out.println("📩 Gửi cuộc gọi đến: " + receiverId);

        messagingTemplate.convertAndSend("/queue/call/"+receiverId, callRequest);
    }
}
