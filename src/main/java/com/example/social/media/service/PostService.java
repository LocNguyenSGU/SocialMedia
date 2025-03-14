package com.example.social.media.service;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PostService {
    PostResponseDTO createPost(PostCreateRequest postCreateRequest,  MultipartFile[] files) throws IOException;
    PageResponse<PostResponseDTO> getPosts(int page, int size, String sortDirection);
    Post getPostById(int postId);
    PostResponseDTO getPostResponseDTOById(int postId);
    PageResponse<PostResponseDTO> getPostsByUserId(int page, int size, String sortDirection, int userId);

    Post createPost(Post post);

    void updateTotalNumberElementPost(String type, int postId);

    PostResponseDTO updatePost(int postId, PostUpdateRequestDTO postUpdateRequestDTO);

    List<Map<String, Object>> getPostStatisticsPerDay();
    List<Map<String, Object>> getPostStatisticsPerMonth();
    List<Map<String, Object>> getPostStatisticsPerYear();

}
