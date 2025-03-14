package com.example.social.media.payload.request.ConversationDTO;

import java.util.List;

public class CreateGroupConversationRequest {
    private String name;
    private List<Integer> participantIds;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
    }
}
