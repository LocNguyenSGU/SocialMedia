package com.example.social.media.entity;

import com.example.social.media.service.SearchRedisService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class PostListener {
    private static final Logger logger = LoggerFactory.getLogger(PostListener.class);

    private final SearchRedisService searchRedisService;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void clearCache(Post post) {
        String pattern = "all_search:" + post.getContent() + "*";
        searchRedisService.deleteByPattern(pattern);
    }

}
