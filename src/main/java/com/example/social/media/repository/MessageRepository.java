package com.example.social.media.repository;

import com.example.social.media.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("select m from Message m where m.conversationId.conversationId = :conversationId " +
            "and m.isDelete = false " +
            "and m.sender.userId = :idUser " +
            "and (:lastMessageTime IS NULL OR m.createdAt < :lastMessageTime) " +
            "order by m.createdAt desc")
    List<Message> findMessageByConversationWithPagination ( @Param("idUser") int idUser,
                                                            @Param("conversationId") int conversationId,
                                                            @Param("lastMessageTime") LocalDateTime lastMessageTime,
                                                            Pageable pageable);
}
