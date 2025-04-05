package com.example.social.media.repository;

import com.example.social.media.entity.PostEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostEmotionRepository extends JpaRepository<PostEmotion, Integer> {

    @Query("SELECT DATE(pe.createdAt) as date, " +
            "COUNT(pe) as emotionCount FROM PostEmotion pe " +
            "GROUP BY DATE(pe.createdAt) " +
            "ORDER BY DATE(pe.createdAt)")
    List<Object[]> countPostEmotionsPerDay();


    @Query("SELECT FUNCTION('YEAR', pe.createdAt) as year, " +
            "FUNCTION('MONTH', pe.createdAt) as month, " +
            "COUNT(pe) as emotionCount FROM PostEmotion pe " +
            "GROUP BY FUNCTION('YEAR', pe.createdAt), FUNCTION('MONTH', pe.createdAt) " +
            "ORDER BY year, month")
    List<Object[]> countPostEmotionsPerMonth();


    @Query("SELECT FUNCTION('YEAR', pe.createdAt) as year, " +
            "COUNT(pe) as emotionCount FROM PostEmotion pe " +
            "GROUP BY FUNCTION('YEAR', pe.createdAt) " +
            "ORDER BY year")
    List<Object[]> countPostEmotionsPerYear();

    // Xóa PostEmotion theo postId và userId
    void deleteByPost_PostIdAndUser_UserId(int postId, int userId);
    boolean existsByPost_PostIdAndUser_UserId(int postId, int userId);
}
