package com.example.social.media.service;

import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;

public interface NotificationRabbitMQService {
    void sendNotification(NotificationMessage<NotificationRequestDTO> notificationMessage);
}
