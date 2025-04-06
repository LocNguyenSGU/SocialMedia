package com.example.social.media.service;

import com.example.social.media.payload.common.NotificationMessage;

public interface NotificationConsumer {
    void receiveMessage(NotificationMessage<?> notificationMessage);
}
