package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import com.example.social.media.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class CommentController {
    CommentService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public DataResponse<PageResponse<CommentResponseDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ){
        PageResponse<CommentResponseDTO> pageResponse = service.getListComment(page, size, sort);
        return DataResponse.<PageResponse<CommentResponseDTO>>builder()
                .message("Lay danh sach bai post")
                .data(pageResponse)
                .build();

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/comment_closer/{parentId}/{postId}")
    public DataResponse<List<CommentResponseDTO>> getDescendant(
            @PathVariable("parentId") int parentId,
            @PathVariable("postId") int postId
    ){
        log.info("Get Descendant:" + parentId);
        return DataResponse.<List<CommentResponseDTO>>builder()
                .data(service.getCommentCloser(parentId , postId))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public DataResponse<CommentResponseDTO> create(@Valid @RequestBody CommentCreateRequest request){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.create(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<CommentResponseDTO> update(@Valid @RequestBody CommentUpdateRequest request ,
                                                   @PathVariable("id") int id){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.update(id , request))
                .build();
    }

    @PostMapping("/reply/{parent-id}")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<CommentResponseDTO> reply(@PathVariable("parent-id") Integer id ,
                                                  @RequestBody CommentCreateRequest request ){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.replyToComment(id , request))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public DataResponse<CommentResponseDTO> getById(@PathVariable("id") int id){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.getById(id))
                .build();
    }


}
