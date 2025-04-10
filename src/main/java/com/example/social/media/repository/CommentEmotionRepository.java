package com.example.social.media.repository;

import com.example.social.media.entity.CommentEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentEmotionRepository extends JpaRepository<CommentEmotion , Integer> {

    Optional<CommentEmotion> findByUser_UserIdAndComment_CommentId(Integer userId, Integer commentId);
    void deleteByUser_UserIdAndComment_CommentId(Integer userId, Integer commentId);

    @Query("SELECT DATE(c.createdAt) as date, " +
            "COUNT(c) as commentCount FROM CommentEmotion c " +
            "GROUP BY DATE(c.createdAt) " +
            "ORDER BY DATE(c.createdAt)")
    List<Object[]> countCommentEmotionsPerDay();


    @Query("SELECT FUNCTION('YEAR', c.createdAt) as year, " +
            "FUNCTION('MONTH', c.createdAt) as month, " +
            "COUNT(c) as commentCount FROM CommentEmotion c " +
            "GROUP BY FUNCTION('YEAR', c.createdAt), FUNCTION('MONTH', c.createdAt) " +
            "ORDER BY year, month")
    List<Object[]> countCommentEmotionsPerMonth();


    @Query("SELECT FUNCTION('YEAR', c.createdAt) as year, " +
            "COUNT(c) as commentCount FROM CommentEmotion c " +
            "GROUP BY FUNCTION('YEAR', c.createdAt) " +
            "ORDER BY year")
    List<Object[]> countCommentEmotionsPerYear();
}
