package com.example.social.media.repository;

import com.example.social.media.entity.CommentEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEmotionRepository extends JpaRepository<CommentEmotion , Integer> {
}
