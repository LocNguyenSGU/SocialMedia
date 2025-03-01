package com.example.social.media.repository;

import com.example.social.media.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend , Integer> {
}
