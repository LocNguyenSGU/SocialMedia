package com.example.social.media.controller;


import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.FriendDTO.FriendUpdateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
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
@RequestMapping("/friend")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor

public class FriendController {

    FriendService friendService;

    @GetMapping("/getlistfriends/{userId}")
    public DataResponse<List<FriendResponseDTO>> getDsFriends(@PathVariable("userId") int userId){
        return DataResponse.<List<FriendResponseDTO>>builder()
                .data(friendService.getDsFriends(userId))
                .message(!friendService.getDsFriends(userId).isEmpty() ? "Đã tìm thấy" : "Không tim thấy")
                .build();
    }

    @GetMapping("/getlistfriendsblocked/{userId}")
    public DataResponse<List<FriendResponseDTO>> getDsFriendsBlocked(@PathVariable("userId") int userId){
        return DataResponse.<List<FriendResponseDTO>>builder()
                .data(friendService.getDsFriendsBlockByUser(userId))
                .message(!friendService.getDsFriendsBlockByUser(userId).isEmpty() ? "Đã tìm thấy" : "Không tim thấy")
                .build();
    }

    @GetMapping("/searchFriends/{userId}")
    public DataResponse<List<FriendResponseDTO>> searchFriends(@PathVariable("userId") int userId  , @RequestParam(defaultValue = "") String keyword){
        return DataResponse.<List<FriendResponseDTO>>builder()
                .data(friendService.searchFriends(userId , keyword))
                .message(friendService.searchFriends(userId , keyword).isEmpty() ? "Không tìm thấy" : "Đã tìm thấy")
                .build();
    }

    @PutMapping("/{id}")
    public DataResponse<FriendResponseDTO> update(@Valid @RequestBody FriendUpdateRequest request , @PathVariable("id") int id){

        return DataResponse.<FriendResponseDTO>builder()
                .data(friendService.update(request , id))
                .message("update thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public DataResponse<FriendResponseDTO> delete(@PathVariable("id") int id){
        return DataResponse.<FriendResponseDTO>builder()
                .data(friendService.delete(id))
                .message("xoá thành công")
                .build();
    }
}
