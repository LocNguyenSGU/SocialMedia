package com.example.social.media.repository;

import com.example.social.media.entity.Post;
import com.example.social.media.enumm.PostVisibilityEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> , JpaSpecificationExecutor<Post> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUser_UserId(Pageable pageable,int userId);


    @Query("SELECT DATE(p.createdAt) as date, " +
            "COUNT(p) as postCount FROM Post p " +
            "GROUP BY DATE(p.createdAt) " +
            "ORDER BY DATE(p.createdAt)")
    List<Object[]> countPostsPerDay();


    @Query("SELECT FUNCTION('YEAR', p.createdAt) as year, " +
            "FUNCTION('MONTH', p.createdAt) as month, " +
            "COUNT(p) as postCount FROM Post p " +
            "GROUP BY FUNCTION('YEAR', p.createdAt), FUNCTION('MONTH', p.createdAt)" +
            "ORDER BY year, month")
    List<Object[]> countPostsPerMonth();


    @Query("SELECT FUNCTION('YEAR', p.createdAt) as year, " +
            "COUNT(p) as postCount FROM Post p " +
            "GROUP BY FUNCTION('YEAR', p.createdAt) " +
            "ORDER BY year")
    List<Object[]> countPostsPerYear();

    Page<Post> findByVisibility(PostVisibilityEnum visibility, Pageable pageable);

}
