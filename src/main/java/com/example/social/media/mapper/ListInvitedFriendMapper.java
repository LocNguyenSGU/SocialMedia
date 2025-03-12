package com.example.social.media.mapper;

import com.example.social.media.entity.ListInvitedFriend;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListInvitedFriendMapper {
    @Mapping(ignore = true, target = "sender")
    @Mapping(ignore = true, target = "receiver")
    ListInvitedFriend toListInvitedFriend(ListInvitedFriendCreateRequest request);
    @Mapping(ignore = true, target = "sender")
    @Mapping(ignore = true, target = "receiver")
    ListInvitedFriendResponseDTO toListInvitedFriendResponseDTO(ListInvitedFriend listInvitedFriend);

    void updateListInvitedFriend(@MappingTarget ListInvitedFriend listInvitedFriend  , ListInvitedFriendUpdateRequest request);
}
