package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
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

    @GetMapping
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

    @PostMapping("/reply/{parent-id}")
    public DataResponse<CommentResponseDTO> reply(@PathVariable("parent-id") Integer id ,
                                                  @RequestBody CommentCreateRequest request ){
        return DataResponse.<CommentResponseDTO>builder()
                .data(service.replyToComment(id , request))
                .build();
    }


}
