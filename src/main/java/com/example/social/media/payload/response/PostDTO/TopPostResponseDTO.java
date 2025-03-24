package com.example.social.media.payload.response.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopPostResponseDTO {
    private int postId;
    private String content;
    private String userFirstName;
    private String userLastName;
    private LocalDateTime createdAt;
    private int numberEmotion;
    private int numberComment;
    private int numberShare;
    private long totalInteraction;
}