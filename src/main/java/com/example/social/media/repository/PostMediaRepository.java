package com.example.social.media.repository;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostMediaRepository extends JpaRepository<PostMedia, Integer> {
    @Override
    void deleteById(Integer integer);


    // Xóa tất cả media bằng truy vấn JPQL
    @Modifying
    @Query("DELETE FROM PostMedia pm WHERE pm.post = :post")
    void deleteByPost(@Param("post") Post post);
}
