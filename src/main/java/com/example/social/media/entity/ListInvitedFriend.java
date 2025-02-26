package com.example.social.media.entity;


import com.example.social.media.enumm.ListInvitedFriendEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "list_invited_friends")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListInvitedFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_invited_friend_id")
    private int listInvitedFriendId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "received_id", nullable = false)
    private User receiver;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ListInvitedFriendEnum status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
