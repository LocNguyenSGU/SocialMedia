package com.example.social.media.service;

import com.example.social.media.payload.request.PostShareDTO.PostShareCreateDTO;
import com.example.social.media.payload.response.PostDTO.PostResponseDTO;

public interface PostShareService {
    PostResponseDTO createPostShare(PostShareCreateDTO postShareCreateDTO);
}
