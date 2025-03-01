package com.example.social.media.mapper;

import com.example.social.media.entity.ListInvitedFriend;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface ListInvitedFriendMapper {
    ListInvitedFriend toListInvitedFriend(ListInvitedFriendCreateRequest request);
    ListInvitedFriendResponseDTO toListInvitedFriendResponseDTO(ListInvitedFriend listInvitedFriend);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListInvitedFriend(@MappingTarget ListInvitedFriend listInvitedFriend  , ListInvitedFriendUpdateRequest request);
}
