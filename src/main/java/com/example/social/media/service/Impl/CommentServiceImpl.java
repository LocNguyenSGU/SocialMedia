package com.example.social.media.service.Impl;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.CommentCloser;
import com.example.social.media.entity.User;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.mapper.CommentMapper;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.CommentDTO.CommentCreateRequest;
import com.example.social.media.payload.request.CommentDTO.CommentUpdateRequest;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import com.example.social.media.repository.CommentCloserRepository;
import com.example.social.media.repository.CommentRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    CommentRepository repository;
    CommentMapper mapper;
    UserRepository userRepository;
    CommentCloserRepository commentCloserRepository;

    @Override
    public CommentResponseDTO create(CommentCreateRequest request) {
        var comment = mapper.toComment(request);
        comment = repository.save(comment);
        return mapper.toCommentResponseDto(comment);
    }

    @Override
    public CommentResponseDTO update(int id , CommentUpdateRequest request) {
        Comment comment = repository.findById(id).orElseThrow(() -> new RuntimeException("Comment not exist"));

        mapper.updateComment(comment , request);

        comment = repository.save(comment);
        return mapper.toCommentResponseDto(comment);
    }

    @Override
    public CommentResponseDTO replyToComment(Integer parentId, CommentCreateRequest request) {
        Comment parentComment = repository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        Comment newComment = mapper.toComment(request);
        newComment = repository.save(newComment);
        CommentCloser commentCloser = new CommentCloser();
        commentCloser.setAncestor(parentComment);
        commentCloser.setDescendant(newComment);
        int depth = commentCloserRepository.findDepthByAncestorId(parentId);
        commentCloser.setDepth(depth+1);
        commentCloserRepository.save(commentCloser);

        return mapper.toCommentResponseDto(newComment);
    }

    @Override
    public PageResponse<CommentResponseDTO> getListComment(int page, int size, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Comment> comments = repository.findAll(pageable);
        Page<CommentResponseDTO> commentDTOs = comments.map(mapper::toCommentResponseDto);
        return new PageResponse<>(commentDTOs);
    }

    @Override
    public CommentResponseDTO getById(int id) {
        Comment comment = repository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_EXITED));

        return mapper.toCommentResponseDto(comment);
    }

    //Statistics
    @Override
    public List<Map<String, Object>> getCommentsStatisticsPerDay() {
        return convertToMapList(repository.countCommentsPerDay(), "date", "count");
    }

    @Override
    public List<Map<String, Object>> getCommentsStatisticsPerMonth() {
        return convertToMapList(repository.countCommentsPerMonth(), "year", "month", "count");
    }

    @Override
    public List<Map<String, Object>> getCommentsStatisticsPerYear() {
        return convertToMapList(repository.countCommentsPerYear(), "year", "count");
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
