package com.example.social.media.mapper;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import com.example.social.media.payload.response.PostMediaDTO.PostMediaResponseDTO;
import com.example.social.media.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostMediaMapper {
    @Autowired
    private PostRepository postRepository;

    PostMedia toPostMedia(PostMediaResponseDTO postMediaResponseDTO, int postIdNew) {
        PostMedia postMedia = new PostMedia();
        postMedia.setMediaUrl(postMediaResponseDTO.getMediaUrl());
        postMedia.setMediaType(postMediaResponseDTO.getMediaType());
        postMedia.setOrder(postMediaResponseDTO.getOrder());
        Optional<Post> postOptional = postRepository.findById(postIdNew);
        postOptional.ifPresent(postMedia::setPost);
        return postMedia;
    }
}
