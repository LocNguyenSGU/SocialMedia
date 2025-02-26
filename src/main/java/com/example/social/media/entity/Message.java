package com.example.social.media.entity;


import com.example.social.media.enumm.CommentTypeEnum;
import com.example.social.media.enumm.MediaTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int messageId;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversationId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_message", nullable = false)
    private MediaTypeEnum typeMessage;

    @Column(name = "content", columnDefinition = "TEXT")
    @Lob
    private String content;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="message_id")
    private List<MessageMedia> messageMediaList;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="message_id")
    private List<MessageStatus> messageStatusList;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="message_id")
    private List<MessageEmotion> messageEmotionList;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
