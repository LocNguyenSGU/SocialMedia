package com.example.social.media.service;

import com.example.social.media.entity.CommentEmotion;
import com.example.social.media.payload.request.CommentDTO.emotion.CommentEmotionCreateRequest;
import com.example.social.media.payload.response.CommentDTO.emotion.CommentEmotionResponseDTO;

import java.util.List;
import java.util.Map;

public interface CommentEmotionService {
    public void toggleLike(CommentEmotionCreateRequest request);

    List<Map<String, Object>> getCommentEmotionsStatisticsPerDay();
    List<Map<String, Object>> getCommentEmotionsStatisticsPerMonth();
    List<Map<String, Object>> getCommentEmotionsStatisticsPerYear();
}
