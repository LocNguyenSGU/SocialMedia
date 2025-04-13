package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import com.example.social.media.entity.PostShare;
import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.mapper.PostMapper;
import com.example.social.media.mapper.PostShareMapper;
import com.example.social.media.payload.request.PostShareDTO.PostShareCreateDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostMediaDTO.PostMediaResponseDTO;
import com.example.social.media.repository.PostMediaRepository;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.PostShareRepository;
import com.example.social.media.service.PostService;
import com.example.social.media.service.PostShareService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostShareServiceImpl implements PostShareService {
    private final PostMediaRepository postMediaRepository;
    PostShareRepository postShareRepository;
    PostShareMapper postShareMapper;
    PostService postService;
    PostMapper postMapper;

    @Override
    public PostResponseDTO createPostShare(PostShareCreateDTO postShareCreateDTO) {
        log.info("Bắt đầu createPostShare...");

        // 1. Map DTO sang entity
        PostShare postShare = postShareMapper.toPostShare(postShareCreateDTO);
        log.info("Mapped PostShare: {}", postShare);

        // 2. Lấy bài viết gốc chỉ với nội dung cần thiết
        PostResponseDTO originalPostDTO = postService.getPostResponseDTOById(postShareCreateDTO.getPostId());
        if (originalPostDTO == null) {
            log.error("Lỗi: Bài viết gốc không tồn tại!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bài viết gốc không tồn tại");
        }
        log.info("Bài viết gốc: {}", originalPostDTO);
        if(!originalPostDTO.getVisibility().equals(PostVisibilityEnum.PUBLIC)) {
            throw new RuntimeException("Khong share được vì bài viết gốc không phải ở chế độ công khai");
        }

        // Cập nhật số lần share trong DB
        postService.updateTotalNumberElementPost("share", originalPostDTO.getPostId());
        log.info("Đã cập nhật số lần share của bài viết gốc: {}", originalPostDTO.getPostId());
        // 3. Tạo bài viết mới cho người share
        Post newPostForUserShare = new Post();
        newPostForUserShare.setUser(postShare.getUser());
        newPostForUserShare.setContent(originalPostDTO.getContent()); // Giữ nguyên nội dung
        newPostForUserShare.setVisibility(postShareCreateDTO.getVisibility()); // Lấy từ DTO
        newPostForUserShare.setTypePost(originalPostDTO.getTypePost()); // Giữ nguyên loại
        newPostForUserShare.setNumberShare(0);
        newPostForUserShare.setNumberComment(0);
        newPostForUserShare.setNumberEmotion(0);
        newPostForUserShare.setCommentList(new ArrayList<>());
        newPostForUserShare.setPostEmotionList(new ArrayList<>());
        newPostForUserShare.setCreatedAt(LocalDateTime.now()); // Fix lỗi missing createdAt


        List<PostMedia> postMediaListNew = new ArrayList<>();

        log.info("Bài viết mới cho người share: {}", newPostForUserShare);

        // 4. Lưu bài viết mới vào DB
        Post postCreatedForUserShare = postService.createPost(newPostForUserShare);
        log.info("Bài viết đã lưu vào DB: {}", postCreatedForUserShare);

        // 4.5 Lưu them anh cua bài viết mới vào DB
        Post old = postService.getPostById(postShareCreateDTO.getPostId());
        List<PostMedia> postMediaList = old.getPostMediaList();
        for(PostMedia p: postMediaList) {
            PostMedia postMedia = new PostMedia();
            postMedia.setMediaType(p.getMediaType());
            postMedia.setMediaUrl(p.getMediaUrl());
            postMedia.setCreatedAt(p.getCreatedAt());
            postMedia.setOrder(p.getOrder());
            postMedia.setPost(postCreatedForUserShare);
            postMediaRepository.save(postMedia);
        }

        // 5. Gán bài viết vừa tạo vào postShare
        postShare.setPost(postCreatedForUserShare);
        postShare.setOriginalPost(postService.getPostById(postShareCreateDTO.getPostId()));
        postShare.setSharedAt(LocalDateTime.now());

        // 6. Lưu postShare vào DB
        PostShare postShareCreated = postShareRepository.save(postShare);

        // 7. Trả về response

        Post resultPostShareNew = postService.getPostById(postShareCreated.getPost().getPostId());
        log.info("Trước khi map: postMediaList = {}", resultPostShareNew.getPostMediaList());
        PostResponseDTO response = postMapper.toPostResponseDTO(resultPostShareNew);
        log.info("Response trả về: {}", response);

        return response;
    }

    //Statistics
    @Override
    public List<Map<String, Object>> getPostSharesStatisticsPerDay() {
        return convertToMapList(postShareRepository.countPostSharesPerDay(), "date", "count");
    }

    @Override
    public List<Map<String, Object>> getPostSharesStatisticsPerMonth() {
        return convertToMapList(postShareRepository.countPostSharesPerMonth(), "year", "month", "count");
    }

    @Override
    public List<Map<String, Object>> getPostSharesStatisticsPerYear() {
        return convertToMapList(postShareRepository.countPostSharesPerYear(), "year", "count");
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
