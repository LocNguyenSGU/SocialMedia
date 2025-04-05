package com.example.social.media.service.Impl;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.repository.PostMediaRepository;
import com.example.social.media.service.PostMediaService;
import com.example.social.media.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostMediaServiceImpl implements PostMediaService {
    PostMediaRepository postMediaRepository;
    @Transactional
    @Override
    public void deleteAllMediaByPost(Post post) {
        postMediaRepository.deleteByPost(post);
    }

    @Override
    public void deletePostMediaById(int id) {
        if (postMediaRepository.existsById(id)) {
            postMediaRepository.deleteById(id); // Xóa PostMedia
            return;
        }
        throw new AppException(ErrorCode.POST_MEDIA_NOT_FOUND);
    }
}
