package com.example.social.media.payload.response.FriendDTO;


import com.example.social.media.enumm.CommentTypeEnum;
import com.example.social.media.enumm.ListInvitedFriendEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Not;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class FriendResponseDTO {
    int user_id ;
    int friend_id;
    Boolean isBlock ;
    int blockByUser;
}
