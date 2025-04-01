package com.example.social.media.mapper;

import com.example.social.media.entity.User;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setIsActive(user.getIsActive());
        dto.setIsOnline(user.getIsOnline());
        dto.setUrlAvatar(user.getUrlAvatar());
        dto.setUrlBackground(user.getUrlBackground());
        return dto;
    }
}

