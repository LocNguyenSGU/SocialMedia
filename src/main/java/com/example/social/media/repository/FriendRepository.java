package com.example.social.media.repository;

import com.example.social.media.entity.Friend;
import com.example.social.media.entity.ListInvitedFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend , Integer> {

    @Query("SELECT f FROM Friend f WHERE (f.user.userId = :loggedInUserId OR f.friend.userId = :loggedInUserId) " +
            "AND f.isBlock = false")
    List<Friend> findFriendsByUserId(@Param("loggedInUserId") int loggedInUserId);

    // Tìm bạn bè theo firstName, lastName, email, hoặc phoneNumber
    // Tìm kiếm bạn bè theo firstName, lastName, phoneNumber, email của người đang đăng nhập
    @Query("SELECT f FROM Friend f " +
            "WHERE (f.user.userId = :userId OR f.friend.userId = :userId) " +
            "AND f.isBlock = false " +
            "AND (LOWER(f.friend.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.friend.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.friend.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.friend.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.user.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    List<Friend> searchFriends(@Param("userId") int UserId,
                               @Param("keyword") String keyword);
}
