package com.example.social.media.service.Impl;

import com.example.social.media.config.RabbitMQConfig;
import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.service.NotificationRabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationRabbitMQServiceImpl implements NotificationRabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void sendNotification(NotificationMessage<?> notificationMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                notificationMessage);
    }
}
