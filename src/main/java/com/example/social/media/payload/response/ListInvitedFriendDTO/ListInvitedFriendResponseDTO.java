package com.example.social.media.payload.response.ListInvitedFriendDTO;

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

public class ListInvitedFriendResponseDTO {
    @NotNull
    int sender ;
    @NotNull
    int receiver ;
    ListInvitedFriendEnum status ;

}
