package com.example.social.media.controller;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostShareDTO.PostShareCreateDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.service.PostShareService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post-shares")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostShareController {
    PostShareService postShareService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public DataResponse<PostResponseDTO> getPostsByUserId(@Valid @RequestBody PostShareCreateDTO postShareCreateDTO) {
        PostResponseDTO postResponseDTO = postShareService.createPostShare(postShareCreateDTO);
        return DataResponse.<PostResponseDTO>builder()
                .message("share bai post")
                .data(postResponseDTO)
                .build();
    }
}
