package com.example.social.media.mapper;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostCreateRequest postCreateRequest);
    @Mapping(source = "user.userId", target = "userId")
    PostResponseDTO toPostResponseDTO(Post post);
}
