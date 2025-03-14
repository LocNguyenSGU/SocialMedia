package com.example.social.media.service.Impl;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.CommentEmotion;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.CommentEmotionMapper;
import com.example.social.media.payload.request.CommentDTO.emotion.CommentEmotionCreateRequest;
import com.example.social.media.payload.response.CommentDTO.emotion.CommentEmotionResponseDTO;
import com.example.social.media.repository.CommentEmotionRepository;
import com.example.social.media.repository.CommentRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.CommentEmotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class CommentEmotionServiceImpl implements CommentEmotionService {
    CommentEmotionMapper mapper;
    CommentEmotionRepository emotionRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;


    @Override
    public CommentEmotionResponseDTO create(CommentEmotionCreateRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommentEmotion commentEmotion = emotionRepository.save(mapper.toCommentEmotion(request));
        return mapper.toCommentEmotionResponseDto(commentEmotion);
    }

    //Statistics
    @Override
    public List<Map<String, Object>> getCommentEmotionsStatisticsPerDay() {
        return convertToMapList(emotionRepository.countCommentEmotionsPerDay(), "date", "count");
    }

    @Override
    public List<Map<String, Object>> getCommentEmotionsStatisticsPerMonth() {
        return convertToMapList(emotionRepository.countCommentEmotionsPerMonth(), "year", "month", "count");
    }

    @Override
    public List<Map<String, Object>> getCommentEmotionsStatisticsPerYear() {
        return convertToMapList(emotionRepository.countCommentEmotionsPerYear(), "year", "count");
    }

    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String... keys) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> data = new HashMap<>();
            for (int i = 0; i < keys.length; i++) {
                data.put(keys[i], row[i]);
            }
            dataList.add(data);
        }
        return dataList;
    }
}
