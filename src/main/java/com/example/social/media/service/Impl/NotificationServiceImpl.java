package com.example.social.media.service.Impl;

import com.example.social.media.entity.Notification;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResponse<NotificationResponseDTO> getNotifies(int page, int size, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Notification> notificationPage = notificationRepository.findAll(pageable);
        Page<NotificationResponseDTO> responseDTOPageResponse = notificationPage.map(notificationMapper::toNotificationResponseDTO);

        return new PageResponse<>(responseDTOPageResponse);
    }

    @Override
    public PageResponse<NotificationResponseDTO> getNotifiesByIdReceiver(int page, int size, String sortDirection, int idReceiver) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Notification> notificationPage = notificationRepository.findByReceiver_UserId(pageable, idReceiver);
        Page<NotificationResponseDTO> responseDTOPageResponse = notificationPage.map(notificationMapper::toNotificationResponseDTO);

        return new PageResponse<>(responseDTOPageResponse);
    }

    @Override
    public Void updateIsReadNotifyById(int idNotify) {
        Notification notification = notificationRepository.findById(idNotify).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return null;
    }

    @Override
    @Transactional
    public Void updateIsReadAllNotifyByIdUser(int idUser) {
        notificationRepository.markAllAsReadByReceiverId(idUser);
        return null;
    }
}
