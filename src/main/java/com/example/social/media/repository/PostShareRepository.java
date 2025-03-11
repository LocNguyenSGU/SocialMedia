package com.example.social.media.repository;

import com.example.social.media.entity.PostShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostShareRepository extends JpaRepository<PostShare, Integer> {
}
