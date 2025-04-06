package com.example.social.media.payload.request.NotificationDTO;

import com.example.social.media.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int receiver;
    private int senderId;
    private String type;
    private int postId;
    private String content;
    private String referenceType;
    private Boolean isRead = false;
    private Boolean isDeleted = false;
}
