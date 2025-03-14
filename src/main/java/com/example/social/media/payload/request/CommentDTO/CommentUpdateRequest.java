package com.example.social.media.payload.request.CommentDTO;

import com.example.social.media.enumm.CommentTypeEnum;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentUpdateRequest {
    @Min(value = 1 , message = "Comment at least 1 character")
    String content;
    CommentTypeEnum typeComment;
    Integer numberEmotion;
    Integer numberCommentChild;
}
