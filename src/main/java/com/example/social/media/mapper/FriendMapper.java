package com.example.social.media.mapper;


import com.example.social.media.entity.Friend;
import com.example.social.media.entity.ListInvitedFriend;
import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.request.FriendDTO.FriendUpdateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")

public interface FriendMapper {
    @Mapping(target = "user.userId" , source = "user_id")
    @Mapping(target = "friend.userId" , source =  "friend_id")
    Friend toFriend(FriendCreateRequest request) ;

    @Mapping(source = "user.userId" , target = "user_id")
    @Mapping(source = "friend.userId" , target =  "friend_id")
    @Mapping(source = "blockBy.userId", target = "blockByUser")
    @Mapping(source = "isBlock" , target = "isBlock")
    FriendResponseDTO toFriendResponseDTO(Friend friend);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFriend(@MappingTarget Friend Friend  , FriendUpdateRequest request);
}
