package com.example.social.media.mapper;

import com.example.social.media.entity.User;
import com.example.social.media.payload.response.AdminDTO.RoleDTO;
import com.example.social.media.payload.response.AdminDTO.UserRole;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse toDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setIsActive(user.getIsActive());
        dto.setIsOnline(user.getIsOnline());
        dto.setUrlAvatar(user.getUrlAvatar());
        dto.setUrlBackground(user.getUrlBackground());
        return dto;
    }

    public UserRole toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        List<RoleDTO> roleRespons = user.getRoles().stream()
                .map(role -> new RoleDTO(role.getName() , role.getDescription()))
                .collect(Collectors.toList());

        return new UserRole(
                user.getUserId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getIsActive(),
                user.getIsOnline(),
                roleRespons
        );
    }


}

