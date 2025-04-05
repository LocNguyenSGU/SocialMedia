package com.example.social.media.service;

import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionDeleteRequest;
import com.example.social.media.payload.response.PostEmotionDTO.PostEmotionResponseDTO;

import java.util.List;
import java.util.Map;

public interface PostEmotionService {
    PostEmotionResponseDTO createEmotion(PostEmotionCreateRequest request);

    List<Map<String, Object>> getPostEmotionsStatisticsPerDay();
    List<Map<String, Object>> getPostEmotionsStatisticsPerMonth();
    List<Map<String, Object>> getPostEmotionsStatisticsPerYear();

    void deletePostEmotion(PostEmotionDeleteRequest postEmotionDeleteRequest);

    // Kiểm tra sự tồn tại của PostEmotion theo postId và userId
}
