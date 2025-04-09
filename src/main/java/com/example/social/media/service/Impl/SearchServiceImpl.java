package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.PostMapper;
import com.example.social.media.mapper.UserMapper;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.SearchRequest.ListSearchRequest;
import com.example.social.media.payload.response.SeachResult.SearchResultResponse;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.SearchRedisService;
import com.example.social.media.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    PostRepository postRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    PostMapper postMapper;
    SearchRedisService searchRedisService;


    @Override
    public PageResponse<SearchResultResponse> searchAll(ListSearchRequest filterRequest) {
        var pageRequest = PageRequest.of(filterRequest.getPage() - 1, filterRequest.getPageSize() /2);

        // Try to get results from Redis cache first
        PageResponse<SearchResultResponse> cachedResults = searchRedisService.getSearch(
                filterRequest.getQuery(),
                pageRequest
        );

        if (cachedResults != null) {
           return cachedResults;
        }



            // If not in cache, perform the database search
            Page<User> userPage = userRepository.findByUserNameContaining(filterRequest.getQuery(), pageRequest);
            Page<Post> postPage = postRepository.findByContentContains(filterRequest.getQuery(), pageRequest);

            long totalUser = userPage.getTotalElements();
            long totalPost = postPage.getTotalElements();
            long totalItems = totalUser + totalPost;

            List<SearchResultResponse> results = new ArrayList<>();

            userPage.forEach(user -> {
                results.add(new SearchResultResponse(userMapper.toDto(user)));
            });

            postPage.forEach(post -> {
                results.add(new SearchResultResponse(postMapper.toPostResponseDTO(post)));
            });

            var page = filterRequest.getPage();
            var pageCount = (int) Math.ceil((double) totalItems / filterRequest.getPageSize());

            // Save results to Redis cache
            searchRedisService.saveSearch(PageResponse.<SearchResultResponse>builder()
                    .data(results)
                    .currentPage(page)
                    .totalPage(pageCount)
                    .pageSize(filterRequest.getPageSize())
                    .totalElements(totalItems)
                    .build(), filterRequest.getQuery(), pageRequest );

            return PageResponse.<SearchResultResponse>builder()
                    .data(results)
                    .currentPage(page)
                    .totalPage(pageCount)
                    .pageSize(filterRequest.getPageSize())
                    .totalElements(totalItems)
                    .build();
        }
}



