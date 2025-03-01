package com.example.social.media.repository;

import com.example.social.media.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Integer> {
    @Query("select c from ConversationMember c " +
            "where c.user.id != :idUser " +
            "and c.conversation.conversationId in " +
            "(SELECT cm2.conversation.id FROM ConversationMember cm2 WHERE cm2.user.id = :idUser)")
    public List<ConversationMember> getConversationMemberByIdUser(@Param("idUser") int idUser);
    @Query("SELECT cm FROM ConversationMember cm WHERE cm.conversation.id = :conversationId")
    List<ConversationMember> findByConversationId(@Param("conversationId") int conversationId);
}
