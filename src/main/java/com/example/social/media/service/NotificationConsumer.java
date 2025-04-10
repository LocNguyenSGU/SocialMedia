package com.example.social.media.service;

import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.response.NotificationDTO.NotificationResponseDTO;

public interface NotificationConsumer {
    void receiveMessage(NotificationMessage<NotificationResponseDTO> notificationMessage);
}
