package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import com.example.social.media.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<List<CommentResponseDTO>> getByPostId(@PathVariable("postId") int postId){
        return DataResponse.<List<CommentResponseDTO>>builder()
                .data(service.getCommentByPostId(postId))
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

    @GetMapping("/public/{postId}/paginated")
    public ResponseEntity<Page<CommentResponseDTO>> getPublicCommentsByPostIdPaginated(
            @PathVariable int postId,
            @ModelAttribute ListRequest request) {
        Page<CommentResponseDTO> comments = service.findByPostPostId(postId,request);
        return ResponseEntity.ok(comments);
    }


    //statistics
    @GetMapping("/statistics/daily")
    public ResponseEntity<DataResponse> getDailyCommentsStatistics() {
        List<Map<String, Object>> data = service.getCommentsStatisticsPerDay();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu bình luận trong ngày."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng bình luận theo ngày."));
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<DataResponse> getMonthlyCommentsStatistics() {
        List<Map<String, Object>> data = service.getCommentsStatisticsPerMonth();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu bình luận trong tháng."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng bình luận theo tháng."));
    }

    @GetMapping("/statistics/yearly")
    public ResponseEntity<DataResponse> getYearlyCommentsStatistics() {
        List<Map<String, Object>> data = service.getCommentsStatisticsPerYear();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu bình luận trong năm."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng bình luận theo năm."));
    }
}
