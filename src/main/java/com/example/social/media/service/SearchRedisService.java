package com.example.social.media.service;

import com.example.social.media.payload.response.SeachResult.SearchResultResponse;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class SearchRedisService{
    private final RedisCacheManager redisCacheManager;
    private final StringRedisTemplate stringRedisTemplate;

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



    public List<SearchResultResponse> getSearch(String keyword, PageRequest pageRequest) {
        if (!useRedisCache) {
            return null;
        }
        String key = this.getKeyFrom(keyword, pageRequest);
        Cache cache = redisCacheManager.getCache("searchResults");
        return cache != null ? cache.get(key, List.class) : null;
    }

    public void clearSearchResults() {
        Cache cache = redisCacheManager.getCache("searchResults");
        if (cache != null) {
            cache.clear();
        }
    }

    public void saveSearch(List<SearchResultResponse> searchResultResponses,
                                String keyword, PageRequest pageRequest) {
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
