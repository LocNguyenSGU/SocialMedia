package com.example.social.media.service;

import com.example.social.media.entity.Post;

public interface PostMediaService {
    void deleteAllMediaByPost(Post post);
    void deletePostMediaById(int id);

}
