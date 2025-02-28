package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.CommentDTO.emotion.CommentEmotionCreateRequest;
import com.example.social.media.payload.response.CommentDTO.emotion.CommentEmotionResponseDTO;
import com.example.social.media.service.CommentEmotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment-emotions")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class CommentEmotionController {
    CommentEmotionService service;

    @PostMapping
    public DataResponse<CommentEmotionResponseDTO> create(@RequestBody
                                 CommentEmotionCreateRequest request){

        return DataResponse.<CommentEmotionResponseDTO>builder()
                .data(service.create(request))
                .build();
    }
}
