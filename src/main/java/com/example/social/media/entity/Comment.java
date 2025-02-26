package com.example.social.media.entity;


import com.example.social.media.enumm.CommentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", columnDefinition = "TEXT")
    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_comment", nullable = false)
    private CommentTypeEnum typeComment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "number_emotion")
    private int numberEmotion;

    @Column(name = "number_comment_child")
    private int numberCommentChild;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="comment_id")
    private List<CommentEmotion> commentEmotionList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="comment_id")
    private List<CommentMedia> commentMediaList;

    @OneToMany(mappedBy = "ancestor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentCloser> ancestors;

    @OneToMany(mappedBy = "descendant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentCloser> descendants;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}