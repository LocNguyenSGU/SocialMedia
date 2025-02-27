package com.example.social.media.controller;

import com.example.social.media.entity.Comment;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import com.example.social.media.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class CommentController {
    CommentService service;

    @PostMapping
    public DataResponse<CommentResponseDTO> create(@Valid @RequestBody CommentCreateRequest request){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public DataResponse<CommentResponseDTO> update(@Valid @RequestBody CommentUpdateRequest request ,
                                                   @PathVariable("id") int id){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.update(id , request))
                .build();
    }




}
