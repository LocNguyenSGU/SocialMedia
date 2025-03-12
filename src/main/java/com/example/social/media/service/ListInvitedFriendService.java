package com.example.social.media.service;

import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;

import java.util.List;

public interface ListInvitedFriendService {
    public ListInvitedFriendResponseDTO create(ListInvitedFriendCreateRequest request);
    public ListInvitedFriendResponseDTO upadte(ListInvitedFriendUpdateRequest request , int id );
    public List<ListInvitedFriendResponseDTO> getDsBySenderId(int senderId) ;
    public List<ListInvitedFriendResponseDTO> getDsByReceiverId(int receiverId) ;
}
