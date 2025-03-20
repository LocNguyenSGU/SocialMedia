package com.example.social.media.service;


import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUserProfile(int userId);
    Optional<UserResponse> updateUserProfile(int userId, UserUpdateRequest userUpdateRequest);
    Optional<UserResponse> updateUserAvatar(int userId, AvatarUpdateRequest avatarUpdateRequest);
    boolean isUserNameExists(String userName);
    boolean isEmailExists(String email);
    List<UserResponse> getDsUsers();
    UserResponse updateActive(int userId , Boolean isActive);

    List<UserResponse> getDsUsersByKeyword(String keyword);
    UserResponse getUserInfo(int userId);

}

