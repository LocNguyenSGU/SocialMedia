package com.example.social.media.entity;

import com.example.social.media.enumm.MediaTypeEnum;
import com.example.social.media.enumm.PostTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_media_id")
    private int messageMediaId;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaTypeEnum mediaType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "media_order")
    private int order;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}