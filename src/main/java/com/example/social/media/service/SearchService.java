package com.example.social.media.service;


import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.SearchRequest.ListSearchRequest;
import com.example.social.media.payload.response.SeachResult.SearchResultResponse;

public interface SearchService {
    public PageResponse<SearchResultResponse> searchAll(ListSearchRequest filterRequest);
}
