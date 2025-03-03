package com.example.social.media.repository;

import com.example.social.media.entity.ListInvitedFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListInvitedFriendRepository extends JpaRepository<ListInvitedFriend , Integer  > {
    // Sử dụng JPQL: giả sử trong entity User trường định danh là "id"
    @Query("select l from ListInvitedFriend l where l.sender.userId = :senderId AND l.status = 'SENT'")
    List<ListInvitedFriend> getListBySenderId(@Param("senderId") Integer senderId);
    @Query("select l from ListInvitedFriend l where l.receiver.userId = :receiverId AND l.status = 'SENT'")
    List<ListInvitedFriend> getListByReceiverId(@Param("receiverId") Integer receiverId);
    @Query("select l from ListInvitedFriend l where l.receiver.userId = :receiverId AND l.sender.userId = :senderId AND l.status = 'SENT'")
    Optional<ListInvitedFriend> filterReceiverAndSenderExist(@Param("receiverId") Integer receiverId , @Param("senderId") Integer senderId);
}
