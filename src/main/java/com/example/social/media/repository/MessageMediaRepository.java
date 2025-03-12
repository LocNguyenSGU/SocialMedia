package com.example.social.media.repository;

import com.example.social.media.entity.MessageMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMediaRepository extends JpaRepository<MessageMedia, Integer> {
}
