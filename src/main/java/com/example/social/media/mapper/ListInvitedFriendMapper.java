package com.example.social.media.mapper;

import com.example.social.media.entity.ListInvitedFriend;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListInvitedFriendMapper {

    @Mapping(target =  "sender.userId" , source = "sender")
    @Mapping(target =  "receiver.userId" , source = "receiver")
    ListInvitedFriend toListInvitedFriend(ListInvitedFriendCreateRequest request);

    @Mapping(target =  "sender" , source = "sender.userId")
    @Mapping(target =  "receiver" , source = "receiver.userId")
    ListInvitedFriendResponseDTO toListInvitedFriendResponseDTO(ListInvitedFriend listInvitedFriend);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListInvitedFriend(@MappingTarget ListInvitedFriend listInvitedFriend  , ListInvitedFriendUpdateRequest request);
}
