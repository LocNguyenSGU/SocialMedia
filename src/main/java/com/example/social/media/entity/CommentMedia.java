package com.example.social.media.entity;

import com.example.social.media.enumm.MediaTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment_media")
    private int idCommentMedia;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

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