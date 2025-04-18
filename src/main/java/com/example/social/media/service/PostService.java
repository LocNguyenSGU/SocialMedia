package com.example.social.media.service;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.FakeNews;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostDTO.TopPostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PostService {
    PostResponseDTO createPost(PostCreateRequest postCreateRequest,  MultipartFile[] files) throws IOException;
    PageResponse<PostResponseDTO> getPosts(int page, int size, String sortDirection, String search);
    Post getPostById(int postId);
    PostResponseDTO getPostResponseDTOById(int postId);
    PageResponse<PostResponseDTO> getPostsByUserId(int page, int size, String sortDirection, int userId);

    Post createPost(Post post);
    List<FakeNews> checkFakeNewsByContent(String content) throws Exception;

    void updateTotalNumberElementPost(String type, int postId);
    public void updateTotalNumberElementPost_AndSave(String type, int postId);
    void updateTotalDescNumberElementPost_AndSave(String type, int postId);
    PostResponseDTO updatePost(int postId, PostUpdateRequestDTO postUpdateRequest, MultipartFile[] newFiles) throws IOException;

    List<Map<String, Object>> getPostStatisticsPerDay();
    List<Map<String, Object>> getPostStatisticsPerMonth();
    List<Map<String, Object>> getPostStatisticsPerYear();
    List<TopPostResponseDTO> getTop5PostsByInteraction(LocalDateTime startDate, LocalDateTime endDate);
    List<TopPostResponseDTO> getTop5PostsByTimeFrame(String timeFrame, Integer week, Integer month, Integer year);
    Page<PostResponseDTO> findByVisibility(ListRequest listRequest);
    List<FakeNews> checkFakeNews(int postId) throws Exception;
    Map<String, Object> getPostStats(LocalDate startDate, LocalDate endDate);

    List<Object[]> getPostCountByVisibility();
    List<Object[]> getPostCountByUser();
    List<Object[]> getTopUsersByPostCount(int limit);
    Map<String, Long> getTotalStatsBetween(LocalDate start, LocalDate end);
}
