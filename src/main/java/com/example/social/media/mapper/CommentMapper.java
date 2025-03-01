package com.example.social.media.mapper;

import com.example.social.media.entity.Comment;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreateRequest request);
    CommentResponseDTO toCommentResponseDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(@MappingTarget Comment comment, CommentUpdateRequest request);
}
