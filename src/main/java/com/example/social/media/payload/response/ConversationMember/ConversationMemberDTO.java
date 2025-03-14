package com.example.social.media.payload.response.ConversationMember;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationMemberDTO {
    private int conversationMemberId;
    private int userId;
    private LocalDateTime joinedAt;
    private String role;
    private Boolean isActive;
}
