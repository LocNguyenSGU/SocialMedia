package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.User;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.SearchRequest.ListSearchRequest;
import com.example.social.media.payload.response.SeachResult.SearchResultResponse;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.SearchService;
import com.example.social.media.specification.SearchSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final PostRepository postRepository;
    UserRepository userRepository;

    @Override
    public PageResponse<SearchResultResponse>  searchAll(ListSearchRequest filterRequest) {
        Specification<User> userSpec = SearchSpecification.hasUsername(filterRequest.getQuery());
        Specification<Post> postSpec = SearchSpecification.hasContentPost(filterRequest.getQuery());

        var pageRequest = PageRequest.of(filterRequest.getPage() - 1, filterRequest.getPageSize());

        long totalUser = userRepository.count(userSpec);
        long totalPost = postRepository.count(postSpec);
        long totalItems = totalUser + totalPost;

        List<SearchResultResponse> results = new ArrayList<>();

        results.addAll(userRepository.findAll(userSpec, pageRequest)
                .stream().map(SearchResultResponse::new).toList());

        results.addAll(postRepository.findAll(postSpec, pageRequest)
                .stream().map(SearchResultResponse::new).toList());

        log.info(results.toString());

        var page = filterRequest.getPage();
        var pageCount = (int) Math.ceil((double) totalItems / filterRequest.getPageSize());

        return PageResponse.<SearchResultResponse>builder()
                .data(results)
                .currentPage(page)
                .totalPage(pageCount)
                .pageSize(filterRequest.getPageSize())
                .totalElements(totalItems)
                .build();

    }
}
