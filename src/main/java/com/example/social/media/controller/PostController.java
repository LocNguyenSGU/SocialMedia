package com.example.social.media.controller;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostController {
    PostService postService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public DataResponse<PostResponseDTO> create(
            @RequestPart(name = "postCreateRequest") String postCreateRequestJson,
            @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PostCreateRequest postCreateRequest = objectMapper.readValue(postCreateRequestJson, PostCreateRequest.class);

        PostResponseDTO response = postService.createPost(postCreateRequest, files);
        log.info("API Response: {}", response);

        return DataResponse.<PostResponseDTO>builder()
                .data(response)
                .message("Tạo mới bài post")
                .build();
    }
    @GetMapping
    public DataResponse<PageResponse<PostResponseDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        PageResponse<PostResponseDTO> pageResponse = postService.getPosts(page, size, sort);
        return DataResponse.<PageResponse<PostResponseDTO>>builder()
                .message("Lay danh sach bai post")
                .data(pageResponse)
                .build();
    }

    @GetMapping("/user/{userId}")
    public DataResponse<PageResponse<PostResponseDTO>> getPostsByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort,
            @PathVariable int userId
    ) {
        PageResponse<PostResponseDTO> pageResponse = postService.getPostsByUserId(page, size, sort, userId);
        return DataResponse.<PageResponse<PostResponseDTO>>builder()
                .message("Lay danh sach bai post by userID")
                .data(pageResponse)
                .build();
    }
    @PutMapping("/{postId}")
    public DataResponse<PostResponseDTO> update(@PathVariable int postId, @Valid @RequestBody PostUpdateRequestDTO requestDTO) {
        PostResponseDTO response = postService.updatePost(postId, requestDTO);
        log.info("API Response: {}", response); // Kiểm tra dữ liệu trước khi trả về
        return DataResponse.<PostResponseDTO>builder()
                .data(response)
                .message("Sua bai post")
                .build();
    }

    @GetMapping("/check/{postId}")
    public DataResponse<String> checkPostContent(@PathVariable("postId") int postId) {
            String result = postService.deletePost(postId);
            return DataResponse.<String>builder()
                    .data(result)
                    .message("check bai post")
                    .build();
    }

    @GetMapping("/public/paginated")
    public ResponseEntity<Page<PostResponseDTO>> getPublicPostsPaginated(
            @ModelAttribute ListRequest request) {
        Page<PostResponseDTO> posts = postService.findByVisibility(request);
        return ResponseEntity.ok(posts);
    }

    //statistics
    @GetMapping("/statistics/daily")
    public ResponseEntity<DataResponse> getDailyPostStatistics() {
        List<Map<String, Object>> data = postService.getPostStatisticsPerDay();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu bài viết trong ngày."));
        } else {
            return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng bài viết theo ngày."));
        }
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<DataResponse> getMonthlyPostStatistics() {
        List<Map<String, Object>> data = postService.getPostStatisticsPerMonth();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu bài viết trong tháng."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng bài viết theo tháng."));
    }

    @GetMapping("/statistics/yearly")
    public ResponseEntity<DataResponse> getYearlyPostStatistics() {
        List<Map<String, Object>> data = postService.getPostStatisticsPerYear();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu bài viết trong năm."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng bài viết theo năm."));
    }

}
