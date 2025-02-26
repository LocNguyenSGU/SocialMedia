package com.example.social.media.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_share")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_share_id")
    private int postShareId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "shared_at", nullable = false)
    private LocalDateTime sharedAt;

    @PrePersist
    protected void onCreate() {
        this.sharedAt = LocalDateTime.now();
    }
}