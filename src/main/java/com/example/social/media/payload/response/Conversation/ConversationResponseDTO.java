package com.example.social.media.payload.response.Conversation;

import com.example.social.media.payload.response.ConversationMember.ConversationMemberDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConversationResponseDTO {
    private int conversationId;
    private String name;
    private Boolean isGroup;
    private int createdBy;
    private LocalDateTime createdAt;
    private List<ConversationMemberDTO> members;
}
