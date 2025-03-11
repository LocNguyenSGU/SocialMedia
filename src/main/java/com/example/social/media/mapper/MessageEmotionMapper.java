package com.example.social.media.mapper;

import com.example.social.media.entity.MessageEmotion;
import com.example.social.media.payload.response.MessageEmotionDTO.MessageEmotionDTO;

public class MessageEmotionMapper {
    public static MessageEmotionDTO mapToDTO(MessageEmotion messageEmotion){
        MessageEmotionDTO messageEmotionDTO = new MessageEmotionDTO();
        messageEmotionDTO.setIdUser(messageEmotion.getUser().getUserId());
        messageEmotionDTO.setEmotion(messageEmotion.getEmotion());
        return messageEmotionDTO;
    }
}
