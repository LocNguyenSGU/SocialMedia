package com.example.social.media.payload.request.ListInvitedFriend;

import com.example.social.media.enumm.ListInvitedFriendEnum;
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

public class ListInvitedFriendCreateRequest {
    @NotNull
    int sender;
    @NotNull
    int receiver;
    ListInvitedFriendEnum status ;
}
