package com.example.social.media.mapper;

import com.example.social.media.entity.Notification;
import com.example.social.media.entity.User;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.response.NotificationDTO.NotificationResponseDTO;
import com.example.social.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    @Autowired
    UserService userService;

    public Notification toEntity(NotificationRequestDTO notificationRequestDTO) {
        if (notificationRequestDTO == null) {
            return null;
        }

        Notification notification = new Notification();
        User userReceiver = userService.getUserById(notificationRequestDTO.getReceiver());
        User userSender = userService.getUserById(notificationRequestDTO.getSenderId());
        notification.setReceiver(userReceiver);
        notification.setSender(userSender);
        notification.setType(notificationRequestDTO.getType());
        notification.setPostId(notificationRequestDTO.getPostId());
        notification.setContent(notificationRequestDTO.getContent());
        notification.setReferenceType(notificationRequestDTO.getReferenceType());
        return notification;
    }

    public NotificationResponseDTO toNotificationResponseDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationResponseDTO dto = new NotificationResponseDTO();
        User userSender = (notification.getSender());
        dto.setFirstNameSender(userSender.getFirstName());
        dto.setLastNameSender(userSender.getLastName());
        dto.setUrlAvatarSender(userSender.getUrlAvatar());
        dto.setType(notification.getType());
        dto.setPostId(notification.getPostId());
        dto.setContent(notification.getContent());
        dto.setReferenceType(notification.getReferenceType());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setIsDeleted(notification.getIsDeleted());

        return dto;
    }

}
