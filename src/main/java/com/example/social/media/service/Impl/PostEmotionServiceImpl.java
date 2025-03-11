package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostEmotion;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.PostEmotionMapper;
import com.example.social.media.payload.request.PostEmotionDTO.PostEmotionCreateRequest;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostEmotionServiceImpl implements PostEmotionService {
    PostEmotionRepository postEmotionRepository;
    UserRepository userRepository; // phai thong qua service
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

        return responseDTO;
    }
}
