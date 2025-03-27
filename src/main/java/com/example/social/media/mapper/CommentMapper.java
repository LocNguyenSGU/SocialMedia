package com.example.social.media.mapper;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.User;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreateRequest request);

    @Mapping(source = "user", target = "userName", qualifiedByName = "mapUserName")
    @Mapping(source = "user", target = "authorAvatarUrl", qualifiedByName = "mapUserAvatar")
    CommentResponseDTO toCommentResponseDto(Comment comment);

    @Named("mapUserName")
    default String mapUserToUserName(User user) {
        return (user != null) ? user.getUserName() : null;
    }

    @Named("mapUserAvatar")
    default String mapUserAvatar(User user) {
        return (user != null) ? user.getUrlAvatar() : null;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(@MappingTarget Comment comment, CommentUpdateRequest request);
}
