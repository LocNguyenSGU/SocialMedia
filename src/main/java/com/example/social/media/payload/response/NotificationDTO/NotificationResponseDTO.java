package com.example.social.media.payload.response.NotificationDTO;

import com.example.social.media.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private int noticeId;
    private String firstNameSender;
    private String lastNameSender;
    private String urlAvatarSender;
    private String type;
    private int postId;
    private String content;
    private String referenceType;
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    private Boolean isDeleted = false;
}
