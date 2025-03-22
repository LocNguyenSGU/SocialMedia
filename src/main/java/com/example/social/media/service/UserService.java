package com.example.social.media.service;


import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUserProfile(int userId);
    Optional<UserResponse> updateUserProfile(int userId, UserUpdateRequest userUpdateRequest);
    Optional<UserResponse> updateUserAvatar(int userId, AvatarUpdateRequest avatarUpdateRequest);
    boolean isUserNameExists(String userName);
    boolean isEmailExists(String email);
    Page<UserResponse> getDsUsers(int page , int size);
    UserResponse updateActive(int userId , Boolean isActive);

    Page<UserResponse> getDsUsersByKeyword(String keyword , int page , int size);
    UserResponse getUserInfo(int userId);

}

