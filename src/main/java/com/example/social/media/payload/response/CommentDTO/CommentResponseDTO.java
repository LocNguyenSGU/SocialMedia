package com.example.social.media.payload.response.CommentDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentResponseDTO {
    Integer commentId;
    String authorAvatarUrl;
    String userName;
    String content;
    Integer numberEmotion;
    Integer numberCommentChild;
    LocalDateTime createdAt;
}
