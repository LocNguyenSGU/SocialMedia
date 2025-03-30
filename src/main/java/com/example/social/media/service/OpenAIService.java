package com.example.social.media.service;

import com.example.social.media.payload.common.FakeNews;

import java.util.List;

public interface OpenAIService {
    List<FakeNews> moderatePostContent(String content);
}
