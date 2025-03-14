package com.example.social.media.service.Impl;

import com.example.social.media.entity.Message;
import com.example.social.media.entity.MessageEmotion;
import com.example.social.media.entity.User;
import com.example.social.media.repository.MessageEmotionRepository;
import com.example.social.media.repository.MessageRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.MessageEmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageEmotionServiceImpl implements MessageEmotionService {
    private MessageEmotionRepository messageEmotionRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    @Autowired
    public MessageEmotionServiceImpl(MessageEmotionRepository messageEmotionRepository
            , UserRepository userRepository
            , MessageRepository messageRepository){
        this.messageEmotionRepository = messageEmotionRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }
    @Override
    public MessageEmotion createMessageEmotion(int messageId, String emotion, int userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Not found this id user"));
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("Not found this id message"));
        MessageEmotion me = new MessageEmotion();
        me.setUser(u);
        me.setEmotion(emotion);
        me.setMessage(message);
        if (emotion == null || emotion.trim().isEmpty()) {
            throw new IllegalArgumentException("Emotion cannot be null or empty");
        }
        return messageEmotionRepository.save(me);
    }
}
