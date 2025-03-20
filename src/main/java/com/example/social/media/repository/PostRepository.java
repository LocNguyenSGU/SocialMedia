package com.example.social.media.repository;

import com.example.social.media.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Integer> , JpaSpecificationExecutor<Post> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUser_UserId(Pageable pageable,int userId);
}
