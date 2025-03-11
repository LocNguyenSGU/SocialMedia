package com.example.social.media.repository;

import com.example.social.media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User , Integer>, JpaSpecificationExecutor<User> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
