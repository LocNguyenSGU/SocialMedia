package com.example.social.media.controller;

import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.SearchRequest.ListSearchRequest;
import com.example.social.media.payload.response.SeachResult.SearchResultResponse;
import com.example.social.media.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class SearchController {
    SearchService service;
    @GetMapping
    public ResponseEntity<PageResponse<SearchResultResponse>> search(@ModelAttribute ListSearchRequest request) {
        return ResponseEntity.ok(service.searchAll(request));
    }
}
