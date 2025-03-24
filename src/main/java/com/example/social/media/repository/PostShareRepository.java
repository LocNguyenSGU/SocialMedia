package com.example.social.media.repository;

import com.example.social.media.entity.PostShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostShareRepository extends JpaRepository<PostShare, Integer> {


    @Query("SELECT DATE(ps.sharedAt) as date, " +
            "COUNT(ps) as shareCount FROM PostShare ps " +
            "GROUP BY DATE(ps.sharedAt) " +
            "ORDER BY DATE(ps.sharedAt)")
    List<Object[]> countPostSharesPerDay();

    // Truy vấn số lượng lượt chia sẻ theo tháng
    @Query("SELECT FUNCTION('YEAR', ps.sharedAt) as year, " +
            "FUNCTION('MONTH', ps.sharedAt) as month, " +
            "COUNT(ps) as shareCount FROM PostShare ps " +
            "GROUP BY FUNCTION('YEAR', ps.sharedAt), FUNCTION('MONTH', ps.sharedAt) " +
            "ORDER BY year, month")
    List<Object[]> countPostSharesPerMonth();

    // Truy vấn số lượng lượt chia sẻ theo năm
    @Query("SELECT FUNCTION('YEAR', ps.sharedAt) as year, " +
            "COUNT(ps) as shareCount FROM PostShare ps " +
            "GROUP BY FUNCTION('YEAR', ps.sharedAt) " +
            "ORDER BY year")
    List<Object[]> countPostSharesPerYear();
}
