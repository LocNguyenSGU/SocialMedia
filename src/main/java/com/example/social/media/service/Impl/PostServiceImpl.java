package com.example.social.media.service.Impl;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.Post;
import com.example.social.media.entity.User;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.mapper.PostMapper;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
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

import java.time.LocalDateTime;
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

    @Override
    public PostResponseDTO getPostResponseDTOById(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Khong co post id " + postId));
        return postMapper.toPostResponseDTO(post);
    }

    @Override
    public PageResponse<PostResponseDTO> getPostsByUserId(int page, int size, String sortDirection, int userId) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Post> postPage = postRepository.findByUser_UserId(pageable, userId);
        Page<PostResponseDTO> postResponseDTOS = postPage.map(postMapper::toPostResponseDTO);
        return new PageResponse<>(postResponseDTOS);
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void updateTotalNumberElementPost(String type, int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Khong co post id " + postId));
        if(type.equalsIgnoreCase("comment"))
            post.setNumberComment(post.getNumberComment() + 1);
        else if (type.equalsIgnoreCase("share"))
            post.setNumberShare(post.getNumberShare() + 1);
        else if (type.equalsIgnoreCase("emotion")) {
            post.setNumberEmotion(post.getNumberEmotion() + 1);
        }
    }

    @Override
    public PostResponseDTO updatePost(int postId, PostUpdateRequestDTO postUpdateRequestDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Khong co post id " + postId));
        post.setVisibility(postUpdateRequestDTO.getVisibility());
        post.setContent(postUpdateRequestDTO.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        Post postUpdated =  postRepository.save(post);

        return postMapper.toPostResponseDTO(postUpdated);
    }
}
