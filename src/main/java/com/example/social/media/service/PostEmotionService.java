package com.example.social.media.service;

import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
import com.example.social.media.payload.response.PostEmotionDTO.PostEmotionResponseDTO;

public interface PostEmotionService {
    PostEmotionResponseDTO createEmotion(PostEmotionCreateRequest request);
}
