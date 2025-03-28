package com.example.social.media.payload.request.PostDTO;

import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.util.customEnum.ValidEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdateRequestDTO {
    @Size(max = 5000, message = "Nội dung bài viết không được vượt quá 5000 ký tự")
    String content;

    @NotNull(message = "Trạng thái hiển thị không được để trống")
    @ValidEnum(enumClass = PostVisibilityEnum.class)
    PostVisibilityEnum visibility;
    private boolean removeOldMedia;
}
