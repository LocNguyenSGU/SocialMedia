package com.example.social.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_emotion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEmotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_emotion_id")
    private int messageEmotionId;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "emotion", nullable = false)
    private String emotion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
