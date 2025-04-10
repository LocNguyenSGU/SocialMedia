package com.example.social.media.payload.response.CommentDTO.emotion;

import com.example.social.media.enumm.CommentEmotionEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentEmotionResponseDTO {
    Long userId;
    Long commentId;
    CommentEmotionEnum emotion;
}
