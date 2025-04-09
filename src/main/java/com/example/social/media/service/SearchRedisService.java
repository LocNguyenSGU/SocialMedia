package com.example.social.media.service;

import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.response.SeachResult.SearchResultResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchRedisService{
    private final RedisCacheManager redisCacheManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.use-redis-cache}")
    private boolean useRedisCache;

    private String getKeyFrom(String keyword, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();

        Sort sort = pageRequest.getSort();
        Sort.Order order = sort.getOrderFor("createdAt");

        String sortDirection = (order != null && order.getDirection() == Sort.Direction.ASC) ? "asc" : "desc";

        return String.format("all_search:%s:%d:%d:%s", keyword, pageNumber, pageSize, sortDirection);
    }



    public PageResponse<SearchResultResponse> getSearch(String keyword, PageRequest pageRequest) {
        if (!useRedisCache) {
            return null;
        }
        String key = this.getKeyFrom(keyword, pageRequest);
        Cache cache = redisCacheManager.getCache("searchResults");
        if (cache == null) return null;

        Object cached = cache.get(key, Object.class);
        if (cached == null) return null;

        return objectMapper.convertValue(
                cached,
                new TypeReference<PageResponse<SearchResultResponse>>() {}
        );
    }



    public void saveSearch(PageResponse<SearchResultResponse>  searchResultResponses,
                           String keyword, PageRequest pageRequest ) {
        String key = this.getKeyFrom(keyword, pageRequest);
        Cache cache = redisCacheManager.getCache("searchResults");
        if (cache != null) {
            cache.put(key, searchResultResponses);
        }
    }


    public void deleteByPattern(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }



}
