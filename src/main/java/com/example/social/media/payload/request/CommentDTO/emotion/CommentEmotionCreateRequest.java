package com.example.social.media.payload.request.CommentDTO.emotion;

import com.example.social.media.enumm.CommentEmotionEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEmotionCreateRequest {
    Integer userId;
    Integer commentId;
    CommentEmotionEnum emotion;
}
