package com.example.social.media.service;

import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.NotificationDTO.NotificationRequestDTO;
import com.example.social.media.payload.response.NotificationDTO.NotificationResponseDTO;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO);
    PageResponse<NotificationResponseDTO> getNotifies(int page, int size, String sortDirection);
    PageResponse<NotificationResponseDTO> getNotifiesByIdReceiver(int page, int size, String sortDirection, int idReceiver);
    Void updateIsReadNotifyById(int idNotify);
    Void updateIsReadAllNotifyByIdUser(int idUser);

}
