package com.example.social.media.mapper;

import com.example.social.media.entity.PostShare;
import com.example.social.media.payload.request.PostShareDTO.PostShareCreateDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostShareMapper {
    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "postId", target = "post.postId")
    PostShare toPostShare(PostShareCreateDTO postShareCreateDTO);

}
