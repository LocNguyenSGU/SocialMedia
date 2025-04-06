package com.example.social.media.service.Impl;

import com.example.social.media.config.RabbitMQConfig;
import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
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
    public void receiveMessage(NotificationMessage<NotificationRequestDTO> notificationMessage) {
        boolean userOnline = checkIfUserOnline(notificationMessage.getIdReceiver());

        if (userOnline) {
            System.out.println("Da gui cho online");
//            messagingTemplate.convertAndSendToUser(notification.getUserId(), "/queue/notifications", notification);
        } else {
            System.out.println("Da gui cho offline");
            String emailReceiver = userService.getUserById(notificationMessage.getIdReceiver()).getEmail();
            System.out.println("Email se nhan duoc thong bao thuc te: " + emailReceiver);
            NotificationRequestDTO notificationRequestDTO = (NotificationRequestDTO) notificationMessage.getObject();
            System.out.println("Noi dung thong bao thuc te: " + notificationRequestDTO.getContent());
            emailService.sendEmail("lockbangss@gmail.com", "Social media", "Thong bao");
        }
    }

    private boolean checkIfUserOnline(int userId) {
        // Implement check online (Redis, session map, etc.)
        return false;
    }
}
