package com.example.social.media.service;

import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;

public interface CommentService {
    public CommentResponseDTO create(CommentCreateRequest request);
    public CommentResponseDTO update(int id ,CommentUpdateRequest request);
}
