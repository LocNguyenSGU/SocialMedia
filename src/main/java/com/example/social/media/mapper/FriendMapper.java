package com.example.social.media.mapper;


import com.example.social.media.entity.Friend;
import com.example.social.media.entity.ListInvitedFriend;
import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.request.FriendDTO.FriendUpdateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface FriendMapper {
    Friend toFriend(FriendCreateRequest request) ;
    FriendResponseDTO toFriendResponseDTO(Friend friend);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFriend(@MappingTarget Friend Friend  , FriendUpdateRequest request);
}
