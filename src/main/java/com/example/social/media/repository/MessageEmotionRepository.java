package com.example.social.media.repository;

import com.example.social.media.entity.MessageEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEmotionRepository extends JpaRepository<MessageEmotion, Integer> {
}
