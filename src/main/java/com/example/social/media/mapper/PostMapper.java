package com.example.social.media.mapper;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostMediaDTO.PostMediaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostCreateRequest postCreateRequest);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "postMediaList", target = "postMedia")
//    @Mapping(source = "commentList", target = "comments")
    PostResponseDTO toPostResponseDTO(Post post);

    // Chuyển từ PostMedia sang PostMediaResponseDTO
    List<PostMediaResponseDTO> toPostMediaResponseDTOList(List<PostMedia> postMediaList);

    @Mapping(source = "post.postId", target = "idPost")
    @Mapping(source = "mediaUrl", target = "mediaUrl")
    @Mapping(source = "mediaType", target = "mediaType")
    @Mapping(source = "order", target = "order")
    PostMediaResponseDTO toPostMediaResponseDTO(PostMedia postMedia);

    // Chuyển từ Comment sang CommentResponseDTO
//    List<CommentResponseDTO> toCommentResponseDTOList(List<Comment> commentList);
//
//    @Mapping(source = "user.userId", target = "userId")
//    @Mapping(source = "commentId", target = "commentId")
//    @Mapping(source = "content", target = "content")
//    @Mapping(source = "createdAt", target = "createdAt")
//    CommentResponseDTO toCommentResponseDTO(Comment comment);
}
