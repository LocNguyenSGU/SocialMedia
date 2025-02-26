package com.example.social.media.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "comment_closer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCloser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_closer_id")
    private int idCommentCloser;

    @ManyToOne
    @JoinColumn(name = "ancestor_id", nullable = false)
    private Comment ancestorId;

    @ManyToOne
    @JoinColumn(name = "descendant_id", nullable = false)
    private Comment descendantId;

    @Column(name = "depth", nullable = false)
    private int depth;
}