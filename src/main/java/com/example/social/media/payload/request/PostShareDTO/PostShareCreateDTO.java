package com.example.social.media.payload.request.PostShareDTO;

import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.util.customEnum.ValidEnum;
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
public class PostShareCreateDTO {
    @NotNull(message = "Post ID không được để trống")
    private int postId; // bai post share

    @NotNull(message = "User ID người share về không được để trống")
    private int userId; // user share - nguoi dang thao tac hanh dong share

    @NotNull(message = "Trạng thái hiển thị không được để trống")
    @ValidEnum(enumClass = PostVisibilityEnum.class)
    private PostVisibilityEnum visibility; // trang thai cua bai post share ve trang cua minh
}
