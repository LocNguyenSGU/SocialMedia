package com.example.social.media.service;


import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUserProfile(int userId);
    Optional<UserResponse> updateUserProfile(int userId, UserUpdateRequest userUpdateRequest);
    Optional<UserResponse> updateUserAvatar(int userId, AvatarUpdateRequest avatarUpdateRequest);
    public Optional<UserResponse>removeUserAvatar(int userId);
    List<Map<String, Object>> getNewUsersPerDay();
    List<Map<String, Object>> getNewUsersPerMonth();
    List<Map<String, Object>> getNewUsersPerYear();

}

