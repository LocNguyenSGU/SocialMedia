package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostEmotionDTO.PostEmotionResponseDTO;
import com.example.social.media.service.PostEmotionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post_emotions")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostEmotionController {
    PostEmotionService postEmotionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public DataResponse<PostEmotionResponseDTO> create(@Valid @RequestBody PostEmotionCreateRequest postEmotionCreateRequest) {
        PostEmotionResponseDTO responseDTO = postEmotionService.createEmotion(postEmotionCreateRequest);
        return DataResponse.<PostEmotionResponseDTO>builder()
                .data(responseDTO)
                .message("Tao moi cam xuc bai post")
                .build();
    }
}
