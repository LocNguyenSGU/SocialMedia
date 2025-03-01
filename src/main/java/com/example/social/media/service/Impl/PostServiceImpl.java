package com.example.social.media.service.Impl;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.Post;
import com.example.social.media.entity.User;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.mapper.PostMapper;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository; // phai la goi thong qua user service -- cai nay de tam thoi

    @Override
    public PostResponseDTO createPost(PostCreateRequest postCreateRequest) {
        User user = userRepository.findById(postCreateRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        new RuntimeException("User not found with ID: " + postCreateRequest.getUserId()))

        Post post = postMapper.toPost(postCreateRequest);
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        log.info("Saved post: {}", savedPost);

        PostResponseDTO responseDTO = postMapper.toPostResponseDTO(savedPost);
        log.info("Response DTO: {}", responseDTO);

        return responseDTO;
    }

    @Override
    public PageResponse<PostResponseDTO> getPosts(int page, int size, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostResponseDTO> postDTOs = posts.map(postMapper::toPostResponseDTO);
        return new PageResponse<>(postDTOs);
    }

    @Override
    public Post getPostById(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("Post not found with ID: " + postId));
        return post;
    }
}
