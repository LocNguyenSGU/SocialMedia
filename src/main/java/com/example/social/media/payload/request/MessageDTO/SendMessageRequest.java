package com.example.social.media.payload.request.MessageDTO;

import com.example.social.media.enumm.MediaTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
    private int conversationId;
    private int senderId;
    private MediaTypeEnum typeMessage;
    private String content;
}
