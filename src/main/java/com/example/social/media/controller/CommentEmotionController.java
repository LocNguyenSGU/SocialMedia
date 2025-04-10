package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.CommentDTO.emotion.CommentEmotionCreateRequest;
import com.example.social.media.payload.response.CommentDTO.emotion.CommentEmotionResponseDTO;
import com.example.social.media.service.CommentEmotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment-emotions")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class CommentEmotionController {
    CommentEmotionService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody
                                 CommentEmotionCreateRequest request){
        service.toggleLike(request);
        return ResponseEntity.ok().build();
    }

    //statistics
    @GetMapping("/statistics/daily")
    public ResponseEntity<DataResponse> getDailyCommentEmotionsStatistics() {
        List<Map<String, Object>> data = service.getCommentEmotionsStatisticsPerDay();

        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu tương tác bình luận trong ngày."));
        }
            return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng tương tác bình luận theo ngày."));

    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<DataResponse> getMonthlyCommentEmotionsStatistics() {
        List<Map<String, Object>> data = service.getCommentEmotionsStatisticsPerMonth();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu tương tác bình luận trong tháng."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng tương tác bình luận theo tháng."));
    }

    @GetMapping("/statistics/yearly")
    public ResponseEntity<DataResponse> getYearlyCommentEmotionsStatistics() {
        List<Map<String, Object>> data = service.getCommentEmotionsStatisticsPerYear();
        if (data.isEmpty()) {
            return ResponseEntity.ok(new DataResponse(204, null, "Không có dữ liệu tương tác bình luận trong năm."));
        }
        return ResponseEntity.ok(new DataResponse(200, data, "Thống kê số lượng tương tác bình luận theo năm."));
    }
}
