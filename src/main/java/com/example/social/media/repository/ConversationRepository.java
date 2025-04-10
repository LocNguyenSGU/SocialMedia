package com.example.social.media.repository;

import com.example.social.media.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    @Query("SELECT c FROM Conversation c " +
            "JOIN c.conversationMemberList m1 ON m1.user.userId = :userId1 " +
            "JOIN c.conversationMemberList m2 ON m2.user.userId = :userId2 " +
            "WHERE c.isGroup = false")
    Optional<Conversation> findPrivateConversationBetweenUsers(@Param("userId1") Integer userId1,
                                                               @Param("userId2") Integer userId2);

}
