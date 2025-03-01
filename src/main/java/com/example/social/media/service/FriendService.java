package com.example.social.media.service;

import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;

public interface FriendService {
    public FriendResponseDTO create(FriendCreateRequest request);
}
