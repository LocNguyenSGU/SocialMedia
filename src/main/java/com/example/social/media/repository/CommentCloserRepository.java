package com.example.social.media.repository;

import com.example.social.media.entity.CommentCloser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentCloserRepository extends JpaRepository<CommentCloser , Integer> {
    @Query("SELECT c.depth FROM CommentCloser c WHERE c.ancestor.id = :ancestorId")
    int findDepthByAncestorId(@Param("ancestorId") int ancestorId);
}
