package com.example.social.media.payload.request.MessageDTO;

import lombok.Data;

@Data
public class VideoCallRequest {
    private String roomId;
    private String callerId;
    private String callerName;
    private String receiverId;

    // getters and setters
}

