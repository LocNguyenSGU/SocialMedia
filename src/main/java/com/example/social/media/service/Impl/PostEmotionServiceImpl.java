package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostEmotion;
import com.example.social.media.entity.User;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.mapper.PostEmotionMapper;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionDeleteRequest;
import com.example.social.media.payload.response.PostEmotionDTO.PostEmotionResponseDTO;
import com.example.social.media.repository.PostEmotionRepository;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.PostEmotionService;
import com.example.social.media.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostEmotionServiceImpl implements PostEmotionService {
    private final PostRepository postRepository;
    PostEmotionRepository postEmotionRepository;
    UserRepository userRepository; // phai thong qua service
    @Lazy
    PostService postService;
    PostEmotionMapper postEmotionMapper;

    @Override
    public PostEmotionResponseDTO createEmotion(PostEmotionCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));
        Post post = postService.getPostById(request.getPostId());

        PostEmotion postEmotion = postEmotionMapper.toPostEmotion(request);
        postEmotion.setUser(user);
        postEmotion.setPost(post);

        PostEmotion postEmotionSaved = postEmotionRepository.save(postEmotion);
        PostEmotionResponseDTO responseDTO = postEmotionMapper.toPostEmotionResponseDTO(postEmotionSaved);
        postService.updateTotalNumberElementPost_AndSave("emotion", request.getPostId());
        return responseDTO;
    }

    //Statistics
    @Override
    public List<Map<String, Object>> getPostEmotionsStatisticsPerDay() {
        return convertToMapList(postEmotionRepository.countPostEmotionsPerDay(), "date", "count");
    }

    @Override
    public List<Map<String, Object>> getPostEmotionsStatisticsPerMonth() {
        return convertToMapList(postEmotionRepository.countPostEmotionsPerMonth(), "year", "month", "count");
    }

    @Override
    public List<Map<String, Object>> getPostEmotionsStatisticsPerYear() {
        return convertToMapList(postEmotionRepository.countPostEmotionsPerYear(), "year", "count");
    }

    @Override
    @Transactional
    public void deletePostEmotion(PostEmotionDeleteRequest postEmotionDeleteRequest) {
        int postId = postEmotionDeleteRequest.getPostId();
        int userId = postEmotionDeleteRequest.getUserId();
        if(postEmotionRepository.existsByPost_PostIdAndUser_UserId(postId, userId)){
            postEmotionRepository.deleteByPost_PostIdAndUser_UserId(postId, userId);
            postService.updateTotalDescNumberElementPost_AndSave("emotion", postId);
            return;
        }
        throw new AppException(ErrorCode.POST_EMOTION_NOT_FOUND);
    }

    @Override
    public boolean checkExistByPostIdAndUserId(int postId, int userId) {
        return postEmotionRepository.existsByPost_PostIdAndUser_UserId(postId, userId);
    }

    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String... keys) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> data = new HashMap<>();
            for (int i = 0; i < keys.length; i++) {
                data.put(keys[i], row[i]);
            }
            dataList.add(data);
        }
        return dataList;
    }
}
