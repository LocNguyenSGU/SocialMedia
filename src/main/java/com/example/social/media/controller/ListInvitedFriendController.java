package com.example.social.media.controller;


import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import com.example.social.media.service.FriendService;
import com.example.social.media.service.ListInvitedFriendService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listinvitedfriend")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class ListInvitedFriendController {
    ListInvitedFriendService listInvitedFriendService ;

    @PostMapping
    public DataResponse<ListInvitedFriendResponseDTO> create(@Valid @RequestBody ListInvitedFriendCreateRequest request){

        System.out.println("id sender : " + request.getSender());
        System.out.println("id receiver : " +  request.getReceiver());
        if(!request.getStatus().name().equals("SENT"))
            return DataResponse.<ListInvitedFriendResponseDTO>builder()
                    .message("loi moi ket ban phai la status sent")
                    .build();
        return DataResponse.<ListInvitedFriendResponseDTO>builder()
                .data(listInvitedFriendService.create(request))
                .message("da gui loi moi ket ban")
                .build();
    }

    @PutMapping("/{id}")
    public DataResponse<ListInvitedFriendResponseDTO> update(@Valid @RequestBody ListInvitedFriendUpdateRequest request , @PathVariable("id") int id){

        return DataResponse.<ListInvitedFriendResponseDTO>builder()
                .data(listInvitedFriendService.upadte(request , id))
                .build();
    }

    @GetMapping("/sended/{idSender}")
    public DataResponse<List<ListInvitedFriendResponseDTO>> getDsBySenderId(@PathVariable("idSender") int idSender){
        return DataResponse.<List<ListInvitedFriendResponseDTO>>builder()
                .data(listInvitedFriendService.getDsBySenderId(idSender))
                .message("danh sach gui loi moi ket ban")
                .build();
    }
    @GetMapping("/received/{idReceiver}")
    public DataResponse<List<ListInvitedFriendResponseDTO>> getDsByReceiverId(@PathVariable("idReceiver") int idReceiver){
        return DataResponse.<List<ListInvitedFriendResponseDTO>>builder()
                .data(listInvitedFriendService.getDsByReceiverId(idReceiver))
                .message("danh sach nhan loi moi ket ban")
                .build();
    }
}
