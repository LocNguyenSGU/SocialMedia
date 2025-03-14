package com.example.social.media.mapper;

import com.example.social.media.entity.PostEmotion;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
import com.example.social.media.payload.response.PostEmotionDTO.PostEmotionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostEmotionMapper {
    PostEmotion toPostEmotion(PostEmotionCreateRequest postEmotionCreateRequest);

    @Mapping(source = "post.postId", target = "postId")
    @Mapping(source = "user.userId", target = "userId")
    PostEmotionResponseDTO toPostEmotionResponseDTO(PostEmotion postEmotion);
}
