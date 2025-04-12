package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import com.example.social.media.entity.User;
import com.example.social.media.enumm.MediaTypeEnum;
import com.example.social.media.enumm.PostTypeEnum;
import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.mapper.CommentMapper;
import com.example.social.media.mapper.PostMapper;
import com.example.social.media.payload.common.FakeNews;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostDTO.TopPostResponseDTO;
import com.example.social.media.repository.PostMediaRepository;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {
    CommentMapper commentMapper;
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository; // phai la goi thong qua user service -- cai nay de tam thoi
    CloudinaryService cloudinaryService;
    CommentService commentService;
    OpenAIService openAIService;
    PostMediaService postMediaService;
    // Thay vì constructor injection, sử dụng setter injection



    @Override
    public PostResponseDTO createPost(PostCreateRequest postCreateRequest, MultipartFile[] files) throws IOException {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(postCreateRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Chuyển PostCreateRequest thành Post entity
        Post post = postMapper.toPost(postCreateRequest);
        post.setUser(user);

        // Lưu bài viết trước
        post = postRepository.save(post);
        log.info("Saved post: {}", post);

        // Nếu có file thì upload lên Cloudinary
        if (files != null && files.length > 0) {
            int order = 1;
            List<PostMedia> postMediaList = new ArrayList<>();
            for (MultipartFile file : files) {
                Map<String, String> uploadResult = cloudinaryService.uploadFile(file);

                // Kiểm tra null trước khi lấy media type
                String mediaType = Optional.ofNullable(uploadResult.get("type")).orElse("").toLowerCase();
                MediaTypeEnum typeEnum = mediaType.startsWith("image") ? MediaTypeEnum.IMAGE : MediaTypeEnum.VIDEO;

                // Tạo media và set vào post
                PostMedia postMedia = new PostMedia();
                postMedia.setPost(post);
                postMedia.setOrder(order++);
                postMedia.setMediaUrl(uploadResult.get("url"));
                postMedia.setMediaType(typeEnum);
                postMediaList.add(postMedia);
            }
            post.setPostMediaList(postMediaList);
            postRepository.save(post); // Cập nhật bài post với media
        }

        // Map Post thành DTO và trả về
        PostResponseDTO responseDTO = postMapper.toPostResponseDTO(post);
        log.info("Response DTO: {}", responseDTO);

        return responseDTO;
    }

    @Override
    public PageResponse<PostResponseDTO> getPosts(int page, int size, String sortDirection, String search) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Post> posts = postRepository.searchPosts(search, pageable);
        Page<PostResponseDTO> postDTOs = posts.map(postMapper::toPostResponseDTO);
        postDTOs.forEach(postDTO -> {
            List<CommentResponseDTO> comments = commentService.getCommentByPostId(postDTO.getPostId());
            postDTO.setComments(comments);
        });

        return new PageResponse<>(postDTOs);
    }

    @Override
    public Post getPostById(int postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("Post not found with ID: " + postId));
    }

    @Override
    public PostResponseDTO getPostResponseDTOById(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
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
    public void updateTotalNumberElementPost_AndSave(String type, int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (type.equalsIgnoreCase("comment")) {
            post.setNumberComment(post.getNumberComment() + 1);
        } else if (type.equalsIgnoreCase("share")) {
            post.setNumberShare(post.getNumberShare() + 1);
        } else if (type.equalsIgnoreCase("emotion")) {
            post.setNumberEmotion(post.getNumberEmotion() + 1);
        }

        // Lưu thay đổi vào database
        postRepository.save(post);
    }

    @Override
    public void updateTotalDescNumberElementPost_AndSave(String type, int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (type.equalsIgnoreCase("comment")) {
            post.setNumberComment(post.getNumberComment() - 1);
        } else if (type.equalsIgnoreCase("share")) {
            post.setNumberShare(post.getNumberShare() - 1);
        } else if (type.equalsIgnoreCase("emotion")) {
            post.setNumberEmotion(post.getNumberEmotion() - 1);
        }

        // Lưu thay đổi vào database
        postRepository.save(post);
    }

//    @Override
//    public PostResponseDTO updatePost(int postId, PostUpdateRequestDTO postUpdateRequestDTO) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Khong co post id " + postId));
//        post.setVisibility(postUpdateRequestDTO.getVisibility());
//        post.setContent(postUpdateRequestDTO.getContent());
//        post.setUpdatedAt(LocalDateTime.now());
//        Post postUpdated =  postRepository.save(post);
//
//        return postMapper.toPostResponseDTO(postUpdated);
//    }

    @Override
    @Transactional
    public PostResponseDTO updatePost(int postId, PostUpdateRequestDTO postUpdateRequest, MultipartFile[] newFiles) throws IOException {
        // Kiểm tra xem bài post có tồn tại không
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Cập nhật nội dung bài viết
        post.setContent(postUpdateRequest.getContent());
        post.setVisibility(postUpdateRequest.getVisibility());

        // Xử lý danh sách media nếu có file mới
        if (newFiles != null && newFiles.length > 0) {
            // Xóa media cũ nếu được yêu cầu
            if (postUpdateRequest.isRemoveOldMedia()) {
                postMediaService.deleteAllMediaByPost(post);
                post.getPostMediaList().clear();
            }

            List<PostMedia> postMediaList = new ArrayList<>();
            int order = 1;

            for (MultipartFile file : newFiles) {
                Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
                String mediaType = Optional.ofNullable(uploadResult.get("type")).orElse("").toLowerCase();
                MediaTypeEnum typeEnum = mediaType.startsWith("image") ? MediaTypeEnum.IMAGE : MediaTypeEnum.VIDEO;

                // Thêm media mới
                PostMedia postMedia = new PostMedia();
                postMedia.setPost(post);
                postMedia.setOrder(order++);
                postMedia.setMediaUrl(uploadResult.get("url"));
                postMedia.setMediaType(typeEnum);
                postMediaList.add(postMedia);
            }

            post.getPostMediaList().addAll(postMediaList);
        }

        // Lưu bài post đã cập nhật
        post = postRepository.save(post);
        log.info("Updated post: {}", post);

        // Trả về DTO
        PostResponseDTO responseDTO = postMapper.toPostResponseDTO(post);
        log.info("Updated Response DTO: {}", responseDTO);

        return responseDTO;
    }

    //Statistics
    @Override
    public List<Map<String, Object>> getPostStatisticsPerDay() {
        return convertToMapList(postRepository.countPostsPerDay(), "date", "count");
    }

    @Override
    public List<Map<String, Object>> getPostStatisticsPerMonth() {
        return convertToMapList(postRepository.countPostsPerMonth(), "year", "month", "count");
    }

    @Override
    public List<Map<String, Object>> getPostStatisticsPerYear() {
        return convertToMapList(postRepository.countPostsPerYear(), "year", "count");
    }

    @Override
    public List<TopPostResponseDTO> getTop5PostsByInteraction(LocalDateTime startDate, LocalDateTime endDate) {
        List<Post> posts = postRepository.findTop5ByInteraction(startDate, endDate);
        return posts.stream()
                .limit(3)
                .map(post -> {
                    long totalInteraction = post.getNumberEmotion() + post.getNumberComment() + post.getNumberShare();
                    return new TopPostResponseDTO(
                            post.getPostId(),
                            post.getContent(),
                            post.getUser().getFirstName(),
                            post.getUser().getLastName(),
                            post.getCreatedAt(),
                            post.getNumberEmotion(),
                            post.getNumberComment(),
                            post.getNumberShare(),
                            totalInteraction
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TopPostResponseDTO> getTop5PostsByTimeFrame(String timeFrame, Integer week, Integer month, Integer year) {
        if (year == null) {
            throw new IllegalArgumentException("Year is required");
        }

        LocalDateTime startDate;
        LocalDateTime endDate;

        switch (timeFrame.toLowerCase()) {
            case "weekly":
                if (week == null || week < 1 || week > 53) {
                    throw new IllegalArgumentException("Week must be between 1 and 53");
                }
                // Tính ngày đầu tiên và cuối cùng của tuần
                LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
                LocalDate startOfWeek = firstDayOfYear.with(WeekFields.of(Locale.getDefault()).weekOfYear(), week)
                        .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1); // Thứ 2 là ngày đầu tuần
                LocalDate endOfWeek = startOfWeek.plusDays(6); // Chủ nhật là ngày cuối tuần
                startDate = startOfWeek.atStartOfDay();
                endDate = endOfWeek.atTime(23, 59, 59);
                break;

            case "monthly":
                if (month == null || month < 1 || month > 12) {
                    throw new IllegalArgumentException("Month must be between 1 and 12");
                }
                // Tính ngày đầu tiên và cuối cùng của tháng
                LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
                LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
                startDate = firstDayOfMonth.atStartOfDay();
                endDate = lastDayOfMonth.atTime(23, 59, 59);
                break;

            case "yearly":
                // Tính ngày đầu tiên và cuối cùng của năm
                LocalDate firstDayOfYearForYearly = LocalDate.of(year, 1, 1);
                LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
                startDate = firstDayOfYearForYearly.atStartOfDay();
                endDate = lastDayOfYear.atTime(23, 59, 59);
                break;

            default:
                throw new IllegalArgumentException("Invalid timeFrame. Must be 'weekly', 'monthly', or 'yearly'");
        }

        return getTop5PostsByInteraction(startDate, endDate);
    }


    @Override
    public Page<PostResponseDTO> findByVisibility(ListRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(),
                Sort.by(request.getSort()).descending());
        return postRepository.findByVisibility(PostVisibilityEnum.PUBLIC, pageable)
                .map(postMapper::toPostResponseDTO);
    }

    @Override
    public List<FakeNews> checkFakeNews(int postId) throws Exception {
        PostResponseDTO postResponseDTO =  getPostResponseDTOById(postId);
        if(!postResponseDTO.getTypePost().equals(PostTypeEnum.TEXT))
            throw new AppException(ErrorCode.BAD_REQUEST_FAKE_NEWS);

        return openAIService.moderatePostContent(postResponseDTO.getContent());
    }

    @Override
    public List<FakeNews> checkFakeNewsByContent(String content) throws Exception {
        return openAIService.moderatePostContent(content);
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

    @Override
    public Map<String, Object> getPostStats(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // inclusive

        List<Post> posts = postRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        long count = posts.size();

        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("posts", posts);
        return result;
    }

    @Override
    public List<Object[]> getPostCountByVisibility() {
        return postRepository.countPostsByVisibility();
    }

    @Override
    public List<Object[]> getPostCountByUser() {
        return postRepository.countPostsByUser();
    }

    @Override
    public List<Object[]> getTopUsersByPostCount(int limit) {
        return postRepository.topUsersByPostCount(PageRequest.of(0, limit));
    }

    @Override
    public Map<String, Long> getTotalStatsBetween(LocalDate start, LocalDate end) {
        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to = end.plusDays(1).atStartOfDay();

        Object[] result = postRepository.sumReactionsBetweenDates(from, to);

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalEmotions", ((Number) result[0]).longValue());
        stats.put("totalComments", ((Number) result[1]).longValue());
        stats.put("totalShares", ((Number) result[2]).longValue());

        return stats;
    }
}
