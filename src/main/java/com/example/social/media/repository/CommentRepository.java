package com.example.social.media.repository;

import com.example.social.media.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment , Integer> {


    @Query("SELECT DATE(c.createdAt) as date, " +
            "COUNT(c) as commentCount FROM Comment c " +
            "GROUP BY DATE(c.createdAt) " +
            "ORDER BY DATE(c.createdAt)")
    List<Object[]> countCommentsPerDay();


    @Query("SELECT FUNCTION('YEAR', c.createdAt) as year, " +
            "FUNCTION('MONTH', c.createdAt) as month, " +
            "COUNT(c) as commentCount FROM Comment c " +
            "GROUP BY FUNCTION('YEAR', c.createdAt), FUNCTION('MONTH', c.createdAt) " +
            "ORDER BY year, month")
    List<Object[]> countCommentsPerMonth();


    @Query("SELECT FUNCTION('YEAR', c.createdAt) as year, " +
            "COUNT(c) as commentCount FROM Comment c " +
            "GROUP BY FUNCTION('YEAR', c.createdAt) " +
            "ORDER BY year")
    List<Object[]> countCommentsPerYear();
}
