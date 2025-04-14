package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
import com.example.social.media.payload.response.NotificationDTO.NotificationResponseDTO;
import com.example.social.media.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifies")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<PageResponse<NotificationResponseDTO>> getAllNotification(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ){
        return DataResponse.<PageResponse<NotificationResponseDTO>>builder()
                .data(notificationService.getNotifies(page, size, sort))
                .message("Lay all notify")
                .build();
    }

    @GetMapping("/receiver/{idUser}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<PageResponse<NotificationResponseDTO>> getAllNotificationByIdReceiver(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "no") String hasUnRead,
            @PathVariable int idUser
    ){
        return DataResponse.<PageResponse<NotificationResponseDTO>>builder()
                .data(notificationService.getNotifiesByIdReceiver(page, size, sort, idUser, hasUnRead))
                .message("Lay all notify by id nhan")
                .build();
    }

    @PutMapping("/{idNotify}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<?> markReadNotifyByIdNotify(
            @PathVariable int idNotify
    ){
        return DataResponse.<Void>builder()
                .data(notificationService.updateIsReadNotifyById(idNotify))
                .message("Danh dau da doc thong bao")
                .build();
    }

    @PutMapping("/receiver/{idReceiver}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<?> markReadAllNotifyByIdReceiver(
            @PathVariable int idReceiver
    ){
        return DataResponse.<Void>builder()
                .data(notificationService.updateIsReadAllNotifyByIdUser(idReceiver))
                .message("danh dau da doc tat ca thong bao")
                .build();
    }

}
