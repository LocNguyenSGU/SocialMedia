package com.example.social.media.repository;

import com.example.social.media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);
    Optional<User> findByEmail(String email);
}
