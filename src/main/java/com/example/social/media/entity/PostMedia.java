package com.example.social.media.entity;


import com.example.social.media.enumm.MediaTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
@Table(name = "post_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_media_id")
    private int postMediaId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

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
