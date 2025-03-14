package com.example.social.media.repository;

import com.example.social.media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    @Query("SELECT DATE(u.createdAt) as date, " +
            "COUNT(u) as count FROM User u " +
            "GROUP BY DATE(u.createdAt) " +
            "ORDER BY DATE(u.createdAt)")
    List<Object[]> countNewUsersPerDay();

    @Query("SELECT FUNCTION('YEAR', u.createdAt) as year, " +
            "FUNCTION('MONTH', u.createdAt) as month, " +
            "COUNT(u) as count FROM User u " +
            "GROUP BY FUNCTION('YEAR', u.createdAt), " +
            "FUNCTION('MONTH', u.createdAt) ORDER BY year, month")
    List<Object[]> countNewUsersPerMonth();

    @Query("SELECT FUNCTION('YEAR', u.createdAt) as year, " +
            "COUNT(u) as count FROM User u " +
            "GROUP BY FUNCTION('YEAR', u.createdAt) " +
            "ORDER BY year")
    List<Object[]> countNewUsersPerYear();
}

