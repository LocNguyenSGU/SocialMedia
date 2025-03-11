package com.example.social.media.payload.request.PostEmotionDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostEmotionCreateRequest {
    @NotNull
    private int postId;

    @NotNull
    private int userId; // ID của người dùng thả cảm xúc

    @NotNull
    private String emotion; // Cảm xúc "hình" icon => "LIKE", "LOVE", "HAHA", etc. ex: 🥰
}
