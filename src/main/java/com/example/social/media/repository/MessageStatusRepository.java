package com.example.social.media.repository;

import com.example.social.media.entity.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageStatusRepository extends JpaRepository<MessageStatus, Integer> {

}
