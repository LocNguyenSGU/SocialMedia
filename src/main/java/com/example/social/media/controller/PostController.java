package com.example.social.media.controller;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.FakeNews;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostDTO.TopPostResponseDTO;
import com.example.social.media.service.OpenAIService;
import com.example.social.media.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostController {
    @Lazy
    PostService postService;
    OpenAIService openAIService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<PageResponse<PostResponseDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "") String search
    ) {
        PageResponse<PostResponseDTO> pageResponse = postService.getPosts(page, size, sort, search);
        return DataResponse.<PageResponse<PostResponseDTO>>builder()
                .message("Lay danh sach bai post co tim kiem theo content")
                .data(pageResponse)
                .build();
    }
    @GetMapping("/{postId}")
    public DataResponse<PostResponseDTO> getPostResponseDTOById(@PathVariable int postId) {
        PostResponseDTO postResponseDTO = postService.getPostResponseDTOById(postId);
        return DataResponse.<PostResponseDTO>builder()
                .message("Lay bai post by id")
                .data(postResponseDTO)
                .build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('USER')")
    public DataResponse<PostResponseDTO> update(
            @PathVariable int postId,
            @RequestPart("postUpdateRequest") String postUpdateRequest,
            @RequestPart(value = "newFiles", required = false) MultipartFile[] newFiles
    ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PostUpdateRequestDTO postUpdateRequestDTO = objectMapper.readValue(postUpdateRequest, PostUpdateRequestDTO.class);

        // Gọi service để update bài post
        PostResponseDTO response = postService.updatePost(postId, postUpdateRequestDTO, newFiles);
        log.info("API Response: {}", response);

        return DataResponse.<PostResponseDTO>builder()
                .data(response)
                .message("Sửa bài post thành công")
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

    @GetMapping("/statistics/top-interaction-by-timeframe")
    public ResponseEntity<DataResponse> getTop5PostsByTimeFrame(
            @RequestParam("timeFrame") String timeFrame,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam("year") Integer year) {
        try {
            List<TopPostResponseDTO> topPosts = postService.getTop5PostsByTimeFrame(timeFrame, week, month, year);
            if (topPosts.isEmpty()) {
                return ResponseEntity.ok(new DataResponse(204, null, "No posts found for the given time frame."));
            }
            return ResponseEntity.ok(new DataResponse(200, topPosts, "Top 3 posts by interaction retrieved successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new DataResponse(400, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new DataResponse(500, null, "Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/check-fake-news/{postId}")
    public ResponseEntity<DataResponse<List<FakeNews>>> checkFakeNews(@PathVariable int postId) throws Exception {
        return ResponseEntity.ok(new DataResponse<>(200, postService.checkFakeNews(postId), "Kiem duyet noi dung bai post"));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPostStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Object> stats = postService.getPostStats(startDate, endDate);
        return ResponseEntity.ok(stats);
    }
}
