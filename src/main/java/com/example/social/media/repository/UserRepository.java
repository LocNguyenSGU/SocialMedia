package com.example.social.media.repository;

import com.example.social.media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Integer> {
}
