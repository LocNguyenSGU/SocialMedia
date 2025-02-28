package com.example.social.media.payload.response.PostDTO;

import com.example.social.media.enumm.PostTypeEnum;
import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.payload.response.CommentDTO.CommentResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponseDTO {
    int postId;
    int userId;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int numberEmotion;
    int numberComment;
    int numberShare;
    PostVisibilityEnum visibility;
    PostTypeEnum typePost;
    List<CommentResponseDTO> comments = new ArrayList<>();
//    List<PostMediaResponseDTO> postMedia;
}
