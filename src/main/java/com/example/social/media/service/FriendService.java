package com.example.social.media.service;

import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.request.FriendDTO.FriendUpdateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;

import java.util.List;

public interface FriendService {
    public FriendResponseDTO create(FriendCreateRequest request);
    public FriendResponseDTO update(FriendUpdateRequest request ,  int id);
    public FriendResponseDTO delete(int id);
    public List<FriendResponseDTO> getDsFriends(int userId);
    public List<FriendResponseDTO> getDsFriendsBlockByUser(int userId);
    public List<FriendResponseDTO> searchFriends(int userId , String keyword) ;
    public boolean isFriend(int userId , int friendId);
    public int idFriend(int userId , int friendId) ;
}
