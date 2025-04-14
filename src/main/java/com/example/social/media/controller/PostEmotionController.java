package com.example.social.media.controller;

import com.example.social.media.entity.Post;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionDeleteRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostEmotionDTO.PostEmotionResponseDTO;
import com.example.social.media.service.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post_emotions")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostEmotionController {
    PostEmotionService postEmotionService;
    PostService postService;
    NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public DataResponse<PostEmotionResponseDTO> create(@Valid @RequestBody PostEmotionCreateRequest postEmotionCreateRequest) {
        PostEmotionResponseDTO responseDTO = postEmotionService.createEmotion(postEmotionCreateRequest);


        Post post = postService.getPostById(postEmotionCreateRequest.getPostId());

        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
        notificationRequestDTO.setReceiver(post.getUser().getUserId());
        notificationRequestDTO.setSenderId(postEmotionCreateRequest.getUserId());
        notificationRequestDTO.setPostId(postEmotionCreateRequest.getPostId());
        notificationRequestDTO.setContent("Người dùng " + "đã thả cảm xúc bài viết của bạn");
        notificationRequestDTO.setType("emotion");
        notificationService.createNotification(notificationRequestDTO);
        // id nguoi gui // co
        // id nguoi nhan // => Thong qua idPost
        // noi dung // co
        // thoi gian // co
        // luu lai // => luu lai
        return DataResponse.<PostEmotionResponseDTO>builder()
                .data(responseDTO)
                .message("Tao moi cam xuc bai post")
                .build();
    }

    //statistics
    @GetMapping("/statistics/daily")
    public ResponseEntity<DataResponse> getDailyPostEmotionsStatistics() {
        List<Map<String, Object>> data = postEmotionService.getPostEmotionsStatisticsPerDay();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu tương tác bài viết trong ngày."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng tương tác bài viết theo ngày."));
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<DataResponse> getMonthlyPostEmotionsStatistics() {
        List<Map<String, Object>> data = postEmotionService.getPostEmotionsStatisticsPerMonth();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu tương tác bài viết trong tháng."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng tương tác bài viết theo tháng."));
    }

    @GetMapping("/statistics/yearly")
    public ResponseEntity<DataResponse> getYearlyPostEmotionsStatistics() {
        List<Map<String, Object>> data = postEmotionService.getPostEmotionsStatisticsPerYear();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu tương tác bài viết trong năm."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng tương tác bài viết theo năm."));
    }

    @DeleteMapping
    public ResponseEntity<?> deletePostEmotion(@Valid @RequestBody PostEmotionDeleteRequest postEmotionDeleteRequest) {
        postEmotionService.deletePostEmotion(postEmotionDeleteRequest);
        return ResponseEntity.ok(new DataResponse("PostEmotion đã được xóa thành công!", "PostEmotion đã được xóa thành công!"));
    }

    @GetMapping("check-exist-post-emotion/post/{postId}/user/{userId}")
    public ResponseEntity<?> checkExistByPostIdAndUserId(@PathVariable int postId, @PathVariable int userId) {
        Boolean exists = postEmotionService.checkExistByPostIdAndUserId(postId, userId);
        return ResponseEntity.ok(new DataResponse<>(exists, "Kiểm tra đã like hay chưa"));
    }
}
