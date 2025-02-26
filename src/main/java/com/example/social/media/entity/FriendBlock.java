package com.example.social.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_block")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private int blockId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người thực hiện chặn

    @ManyToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private User blockedUser; // Người bị chặn

    @Column(name = "reason")
    private String reason; // Lý do chặn (tùy chọn lý do bên front end hoặc nhập text tuỳ)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
