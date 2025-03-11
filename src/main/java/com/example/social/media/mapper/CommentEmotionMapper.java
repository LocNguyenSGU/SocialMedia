package com.example.social.media.mapper;

import com.example.social.media.entity.CommentEmotion;
import com.example.social.media.payload.request.CommentDTO.emotion.CommentEmotionCreateRequest;
import com.example.social.media.payload.response.CommentDTO.emotion.CommentEmotionResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentEmotionMapper {
    CommentEmotion toCommentEmotion(CommentEmotionCreateRequest request);
    CommentEmotionResponseDTO toCommentEmotionResponseDto(CommentEmotion commentEmotion);
}
