package com.example.social.media.service;

import com.example.social.media.entity.CommentEmotion;
import com.example.social.media.payload.request.CommentDTO.emotion.CommentEmotionCreateRequest;
import com.example.social.media.payload.response.CommentDTO.emotion.CommentEmotionResponseDTO;

public interface CommentEmotionService {
    public CommentEmotionResponseDTO create(CommentEmotionCreateRequest request);
}
