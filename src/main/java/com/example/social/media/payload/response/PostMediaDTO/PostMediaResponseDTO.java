package com.example.social.media.payload.response.PostMediaDTO;

import com.example.social.media.entity.Post;
import com.example.social.media.enumm.MediaTypeEnum;
import jakarta.persistence.*;
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
public class PostMediaResponseDTO {
    int postMediaId;
    int idPost;
    String mediaUrl;
    MediaTypeEnum mediaType;
    LocalDateTime createdAt;
    int order;
}
