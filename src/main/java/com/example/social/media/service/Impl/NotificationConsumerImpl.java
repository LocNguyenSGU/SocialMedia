package com.example.social.media.service.Impl;

import com.example.social.media.config.RabbitMQConfig;
import com.example.social.media.entity.User;
import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.response.NotificationDTO.NotificationResponseDTO;
import com.example.social.media.service.EmailService;
import com.example.social.media.service.NotificationConsumer;
import com.example.social.media.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerImpl implements NotificationConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // dùng WebSocket

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Override
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(NotificationMessage<NotificationResponseDTO> notificationMessage) {
        boolean userOnline = checkIfUserOnline(notificationMessage.getIdReceiver());
        if (userOnline) {
            messagingTemplate.convertAndSend("/topic/notify-" + 66, notificationMessage);
        } else {
            String emailReceiver = userService.getUserById(notificationMessage.getIdReceiver()).getEmail();
            NotificationResponseDTO notificationResponseDTO = (NotificationResponseDTO) notificationMessage.getObject();
            String firstName = notificationResponseDTO.getFirstNameSender();
            String lastName = notificationResponseDTO.getLastNameSender();
            emailService.sendEmail(emailReceiver, "Social media", firstName + " " + lastName + ": " + notificationResponseDTO.getContent());
        }
    }

    private boolean checkIfUserOnline(int userId) {
        User user = userService.getUserById(userId);
        return user.getIsOnline();
    }
}
