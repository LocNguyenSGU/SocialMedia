package com.example.social.media.mapper;

import com.example.social.media.entity.Message;
import com.example.social.media.entity.MessageStatus;
import com.example.social.media.payload.response.MessageDTO.MessageDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {
    public static MessageDTO mapToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setIdSender(message.getSender().getUserId()); // Lấy ID của người gửi
        dto.setTypeMessage(message.getTypeMessage());
        dto.setContent(message.getContent());
        dto.setUpdateAt(message.getUpdateAt());
        dto.setIsDelete(message.getIsDelete());
        dto.setCreatedAt(message.getCreatedAt());

        // Lấy trạng thái tin nhắn (nếu có)
        if (message.getMessageStatusList() != null && !message.getMessageStatusList().isEmpty()) {
            MessageStatus latestStatus = message.getMessageStatusList().get(0);
            dto.setMessageStatus(latestStatus.getMessageStatus());
            dto.setReadAt(latestStatus.getReadAt());
        }

        // Lấy danh sách cảm xúc (nếu có)
        if (message.getMessageEmotionList() != null) {
            dto.setMessageEmotionList(message.getMessageEmotionList().stream().map(MessageEmotionMapper::mapToDTO).collect(Collectors.toList()));
        }

        return dto;
    }

    public static List<MessageDTO> mapToDTOList(List<Message> messages) {
        return messages.stream().map(MessageMapper::mapToDTO).collect(Collectors.toList());
    }
}