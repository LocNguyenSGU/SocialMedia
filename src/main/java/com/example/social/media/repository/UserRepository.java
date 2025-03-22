package com.example.social.media.repository;

import com.example.social.media.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User , Integer>, JpaSpecificationExecutor<User> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User findByUserName(String username);
    User findByUserId(int userId);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword , Pageable pageable);
}
