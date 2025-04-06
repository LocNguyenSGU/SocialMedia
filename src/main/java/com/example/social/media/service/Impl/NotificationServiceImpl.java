package com.example.social.media.service.Impl;

import com.example.social.media.entity.Notification;
import com.example.social.media.mapper.NotificationMapper;
import com.example.social.media.payload.common.NotificationMessage;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.response.NotificationDTO.NotificationResponseDTO;
import com.example.social.media.repository.NotificationRepository;
import com.example.social.media.service.NotificationRabbitMQService;
import com.example.social.media.service.NotificationService;
import com.example.social.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationRabbitMQService notificationRabbitMQService;

    private UserService userService;
    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        Notification notification = notificationMapper.toEntity(notificationRequestDTO);
        Notification notificationSaved = notificationRepository.save(notification);
        int idReceiver = notificationRequestDTO.getReceiver();
        notificationRabbitMQService.sendNotification(new NotificationMessage<>(idReceiver, notificationRequestDTO));
        return notificationMapper.toNotificationResponseDTO(notificationSaved);
    }

    @Override
    public PageResponse<NotificationResponseDTO> getNotifies() {
        return null;
    }
}
