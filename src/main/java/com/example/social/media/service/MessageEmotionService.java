package com.example.social.media.service;

import com.example.social.media.entity.MessageEmotion;
import org.springframework.stereotype.Service;

@Service
public interface MessageEmotionService {
    public MessageEmotion createMessageEmotion(int messageId, String emotion, int idUser);
}
