package com.example.social.media.mapper;

import com.example.social.media.entity.Conversation;
import com.example.social.media.payload.response.Conversation.ConversationResponseDTO;
import com.example.social.media.payload.response.ConversationMember.ConversationMemberDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ConversationMapper {
    public static ConversationResponseDTO mapToDTO(Conversation conversation) {
        ConversationResponseDTO dto = new ConversationResponseDTO();
        dto.setConversationId(conversation.getConversationId());
        dto.setName(conversation.getName());
        dto.setIsGroup(conversation.getIsGroup());
        dto.setCreatedBy(conversation.getCreatedBy());
        dto.setCreatedAt(conversation.getCreatedAt());

        List<ConversationMemberDTO> memberDTOs = conversation.getConversationMemberList()
                .stream()
                .map(member -> {
                    ConversationMemberDTO memberDTO = new ConversationMemberDTO();
                    memberDTO.setConversationMemberId(member.getConversationMemberId());
                    memberDTO.setUserId(member.getUser().getUserId());
                    memberDTO.setJoinedAt(member.getJoinedAt());
                    memberDTO.setRole(member.getRole());
                    memberDTO.setIsActive(member.getIsActive());
                    return memberDTO;
                })
                .collect(Collectors.toList());

        dto.setMembers(memberDTOs);
        return dto;
    }
}
