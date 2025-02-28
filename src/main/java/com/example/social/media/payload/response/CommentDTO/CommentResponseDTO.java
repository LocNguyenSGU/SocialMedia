package com.example.social.media.payload.response.CommentDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentResponseDTO {
    String content;
    Integer numberEmotion;
    Integer numberCommentChild;
}
