package com.example.social.media.service;

import com.example.social.media.entity.Conversation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConversationService {
    public Conversation createNewConversation(Integer createrId, List<Integer> participantId, String conversationName);
    public Conversation createOneToOneConversation(Integer creatorId, Integer otherUserId);
    public Conversation getConversationById(Integer conversationId);
}
