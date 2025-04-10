package com.example.social.media.service;

import com.example.social.media.entity.Comment;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface CommentService {
    public CommentResponseDTO create(CommentCreateRequest request);
    public CommentResponseDTO update(int id ,CommentUpdateRequest request);
    public CommentResponseDTO replyToComment(Integer parentId, CommentCreateRequest request);
    public PageResponse<CommentResponseDTO> getListComment(int page, int size, String sortDirection);
    public CommentResponseDTO getById(int id);
    public List<CommentResponseDTO> getCommentCloser(Integer parentId , Integer postId);
    public List<CommentResponseDTO> getCommentByPostId(Integer postId);
    public boolean checkCommentEmotionByUserIdAndCommentId(Integer userId, Integer commentId);

    List<Map<String, Object>> getCommentsStatisticsPerDay();
    List<Map<String, Object>> getCommentsStatisticsPerMonth();
    List<Map<String, Object>> getCommentsStatisticsPerYear();
    Page<CommentResponseDTO> findByPostPostId(int postId , ListRequest request);
}
