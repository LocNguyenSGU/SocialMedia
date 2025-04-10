package com.example.social.media.repository;

import com.example.social.media.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findAll(Pageable pageable);
    Page<Notification> findByReceiver_UserId(Pageable pageable, int idReceiver);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiver.userId = :idReceiver")
    int markAllAsReadByReceiverId(@Param("idReceiver") int idReceiver);
}
