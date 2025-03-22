package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import com.example.social.media.entity.User;
import com.example.social.media.enumm.MediaTypeEnum;
import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.mapper.PostMapper;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.PostDTO.PostCreateRequest;
import com.example.social.media.payload.request.PostDTO.PostUpdateRequestDTO;
import com.example.social.media.payload.request.SearchRequest.ListRequest;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.PostDTO.TopPostResponseDTO;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.CloudinaryService;
import com.example.social.media.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository; // phai la goi thong qua user service -- cai nay de tam thoi
    CloudinaryService cloudinaryService;

    @Value("${forbidden.words:}")
    @NonFinal
    String forbiddenWordsString;

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
                .limit(5)
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
    public String deletePost(int id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Post not existed"));

        String content = post.getContent().toLowerCase();

        List<String> forbiddenWords = List.of(forbiddenWordsString.split(","));
        for (String forbiddenWord : forbiddenWords) {
            log.info(forbiddenWord);
            if (content.contains(forbiddenWord.trim().toLowerCase())) {
                postRepository.delete(post);
                return "Bài viết đã được kiểm tra và xóa cảm ơn bạn";
            }
        }

        return "Bài viết không chứa từ ngữ vi phạm";

    }

    @Override
    public Page<PostResponseDTO> findByVisibility(ListRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(),
                Sort.by(request.getSort()).descending());
        return postRepository.findByVisibility(PostVisibilityEnum.PUBLIC, pageable)
                .map(postMapper::toPostResponseDTO);
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
