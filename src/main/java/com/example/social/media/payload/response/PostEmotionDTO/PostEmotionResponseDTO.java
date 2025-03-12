package com.example.social.media.payload.response.PostEmotionDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostEmotionResponseDTO {
     int postEmotionId;
     int postId;
     int userId;
     String emotion;
     LocalDateTime createdAt;
}
