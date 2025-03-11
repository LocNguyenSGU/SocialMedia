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
}
