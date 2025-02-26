package com.example.social.media.entity;

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
    private Message messageId;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiverId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "read_at")
    private LocalDateTime readAt;


}