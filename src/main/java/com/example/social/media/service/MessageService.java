package com.example.social.media.service;

import com.example.social.media.entity.Message;
import com.example.social.media.payload.request.MessageDTO.SendMessageRequest;
import com.example.social.media.payload.response.Conversation.ConversationDTO;
import com.example.social.media.payload.response.MessageDTO.MessageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface MessageService {
    //hàm lấy tất cả message trong 1 conversation của 1 user thông qua id user đang đăng nhập và id conversation
    public ConversationDTO getMessageByIdUser(int idUser, int conversationId, LocalDateTime lastMessageTime, int pageSize);
    // hàm lấy danh sách conversation của 1 user bằng id user
    public List<ConversationDTO> getAllConversationByIdUser(int id);
    public Message sendMessage(SendMessageRequest request);
}
