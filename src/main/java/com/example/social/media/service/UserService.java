package com.example.social.media.service;


import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;

import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUserProfile(int userId);
    Optional<UserResponse> updateUserProfile(int userId, UserUpdateRequest userUReq);
    Optional<UserResponse> updateUserAvatar(int userId, AvatarUpdateRequest avatarUReq);
    boolean isUserNameExists(String userName);
    boolean isEmailExists(String email);
}

