package com.example.social.media.service;

import com.example.social.media.payload.request.PostShareDTO.PostShareCreateDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;

import java.util.List;
import java.util.Map;

public interface PostShareService {
    PostResponseDTO createPostShare(PostShareCreateDTO postShareCreateDTO);

    List<Map<String, Object>> getPostSharesStatisticsPerDay();
    List<Map<String, Object>> getPostSharesStatisticsPerMonth();
    List<Map<String, Object>> getPostSharesStatisticsPerYear();
}
