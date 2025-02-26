package com.example.social.media.entity;

import com.example.social.media.enumm.MessageStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_status_id")
    private int messageStatusId;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status", nullable = false)
    private MessageStatusEnum messageStatus;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}