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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public DataResponse<PostEmotionResponseDTO> create(@Valid @RequestBody PostEmotionCreateRequest postEmotionCreateRequest) {
        PostEmotionResponseDTO responseDTO = postEmotionService.createEmotion(postEmotionCreateRequest);
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
}
