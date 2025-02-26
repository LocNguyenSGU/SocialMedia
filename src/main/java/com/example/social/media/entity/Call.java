package com.example.social.media.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id")
    private int callId;

    @Column(name = "conversation_id", nullable = false)
    private int conversationId;

    @Column(name = "caller_id", nullable = false)
    private int callerId;

    @Column(name = "receiver_id", nullable = false)
    private int receiverId;

    @Column(name = "call_type", nullable = false)
    private String callType;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "call_status")
    private String callStatus;
}