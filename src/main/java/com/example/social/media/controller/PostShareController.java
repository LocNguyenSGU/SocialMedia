package com.example.social.media.controller;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.request.PostShareDTO.PostShareCreateDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.service.NotificationService;
import com.example.social.media.service.PostService;
import com.example.social.media.service.PostShareService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post-shares")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostShareController {
    PostShareService postShareService;
    PostService postService;
    NotificationService notificationService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public DataResponse<PostResponseDTO> getPostsByUserId(@Valid @RequestBody PostShareCreateDTO postShareCreateDTO) {
        PostResponseDTO postResponseDTO = postShareService.createPostShare(postShareCreateDTO);
        Post post = postService.getPostById(postShareCreateDTO.getPostId());
        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
        notificationRequestDTO.setReceiver(post.getUser().getUserId());
        notificationRequestDTO.setSenderId(postShareCreateDTO.getUserId());
        notificationRequestDTO.setPostId(postShareCreateDTO.getPostId());
        notificationRequestDTO.setContent("Người dùng " + "đã chia sẽ bài viết của bạn");
        notificationRequestDTO.setType("comment");
        notificationService.createNotification(notificationRequestDTO);
        return DataResponse.<PostResponseDTO>builder()
                .message("share bai post")
                .data(postResponseDTO)
                .build();
    }

    //statistics
    @GetMapping("/statistics/daily")
    public ResponseEntity<DataResponse> getDailyPostSharesStatistics() {
        List<Map<String, Object>> data = postShareService.getPostSharesStatisticsPerDay();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu chia sẻ bài viết trong ngày."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng chia sẻ bài viết theo ngày."));
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<DataResponse> getMonthlyPostSharesStatistics() {
        List<Map<String, Object>> data = postShareService.getPostSharesStatisticsPerMonth();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu chia sẻ bài viết trong tháng."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng chia sẻ bài viết theo tháng."));
    }

    @GetMapping("/statistics/yearly")
    public ResponseEntity<DataResponse> getYearlyPostSharesStatistics() {
        List<Map<String, Object>> data = postShareService.getPostSharesStatisticsPerYear();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu chia sẻ bài viết trong năm."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng chia sẻ bài viết theo năm."));
    }
}
