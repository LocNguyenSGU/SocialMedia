package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.service.PostMediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post-medias")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PostMediaController {
    @Lazy
    PostMediaService postMediaService;
    // Xóa PostMedia theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostMedia(@PathVariable int id) {
         postMediaService.deletePostMediaById(id);
         return ResponseEntity.ok(new DataResponse("PostMedia đã được xóa thành công!", "PostMedia đã được xóa thành công!"));
    }
}
