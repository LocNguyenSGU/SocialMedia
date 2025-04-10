package com.example.social.media.service.Impl;

import com.example.social.media.entity.Conversation;
import com.example.social.media.entity.ConversationMember;
import com.example.social.media.entity.User;
import com.example.social.media.repository.ConversationMemberRepository;
import com.example.social.media.repository.ConversationRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {
    private ConversationRepository conversationRepository;
    private UserRepository userRepository;
    @Autowired
    public ConversationServiceImpl(ConversationRepository conversationRepository, UserRepository userRepository){
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Conversation createNewConversation(Integer creatorId, List<Integer> participantIds, String conversationName) {
        //kiểm tra input
        if (creatorId == null || participantIds == null || participantIds.isEmpty()) {
            throw new IllegalArgumentException("Creator ID and at least one participant are required");
        }

        if (participantIds.size() == 1) {
            // Private chat: kiểm tra tồn tại trước
            Integer participantId = participantIds.get(0);
            Optional<Conversation> existing = conversationRepository.findPrivateConversationBetweenUsers(creatorId, participantId);
            if (existing.isPresent()) {
                return existing.get(); // hoặc throw nếu muốn
            }
        }

        //get các user tham gia group chat
        List<User> listUser = userRepository.findAllById(participantIds);
        User creator = userRepository.findById(creatorId).orElseThrow(()-> new IllegalArgumentException("Creator not found"));
        if(participantIds.size() != listUser.size()){
            throw new IllegalArgumentException("One or more participants not found");
        }
        //tạo conversation
        Conversation conversation = new Conversation();
        conversation.setCreatedBy(creatorId);
        conversation.setIsGroup(listUser.size()>1);
        if (conversationName != null && !conversationName.trim().isEmpty()) {
            conversation.setName(conversationName);
        }
        //tạo các member của conversation
        List<ConversationMember> members = new ArrayList<>();

        //tạo admin group
        ConversationMember creatorMember = new ConversationMember();
        creatorMember.setConversation(conversation);
        creatorMember.setUser(creator);
        creatorMember.setRole("ADMIN"); // Creator mặc định là admin
        creatorMember.setIsActive(true);
        members.add(creatorMember);

        //tạo member other
        for(User u : listUser){
            if(u.getUserId() == creatorId) continue;
            ConversationMember mem = new ConversationMember();
            mem.setConversation(conversation);
            mem.setUser(u);
            mem.setRole("MEMBER"); // Creator mặc định là admin
            mem.setIsActive(true);
            members.add(mem);
        }

        conversation.setConversationMemberList(members);
        return conversationRepository.save(conversation);
    }

    // function để tạo conversation giữa 2 người mới kết bạn
    @Override
    public Conversation createOneToOneConversation(Integer creatorId, Integer otherUserId){
        List<Integer> participantIds = new ArrayList<>();
        participantIds.add(otherUserId);
        return createNewConversation(creatorId, participantIds, null);
    }
    @Override
    public Conversation getConversationById(Integer conversationId) {
        return conversationRepository.findById(conversationId)
                .orElse(null);
    }
}
