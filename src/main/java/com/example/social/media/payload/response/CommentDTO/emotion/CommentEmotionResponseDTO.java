package com.example.social.media.payload.response.CommentDTO.emotion;

import com.example.social.media.enumm.CommentEmotionEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentEmotionResponseDTO {
    Long userId;
    Long commentId;
    CommentEmotionEnum emotion;
}
