package com.example.social.media.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private int friendId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_user_id")
    private User friend;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_block")
    private Boolean isBlock = false;

    @OneToOne
    @JoinColumn(name = "block_by")
    private User blockBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
