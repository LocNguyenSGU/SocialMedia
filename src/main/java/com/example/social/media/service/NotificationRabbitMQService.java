package com.example.social.media.service;

import com.example.social.media.payload.common.NotificationMessage;

public interface NotificationRabbitMQService {
    void sendNotification(NotificationMessage<?> notificationMessage);
}
