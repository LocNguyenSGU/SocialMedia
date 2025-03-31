package com.example.social.media.repository;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment , Integer> {
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
    List<Comment> findCommentsByPostId(@Param("postId") Integer postId);
}
