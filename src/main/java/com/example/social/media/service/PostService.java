package com.example.social.media.service;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostResponseDTO createPost(PostCreateRequest postCreateRequest);
    PageResponse<PostResponseDTO> getPosts(int page, int size, String sortDirection);

    Post getPostById(int postId);

}
