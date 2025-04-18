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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friend")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor

public class FriendController {

    FriendService friendService;

    @GetMapping("/getlistfriends/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @PreAuthorize("hasRole('USER')")
    public DataResponse<List<FriendResponseDTO>> searchFriends(@PathVariable("userId") int userId  , @RequestParam(defaultValue = "") String keyword){
        return DataResponse.<List<FriendResponseDTO>>builder()
                .data(friendService.searchFriends(userId , keyword))
                .message(friendService.searchFriends(userId , keyword).isEmpty() ? "Không tìm thấy" : "Đã tìm thấy")
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<FriendResponseDTO> update(@Valid @RequestBody FriendUpdateRequest request , @PathVariable("id") int id){

        return DataResponse.<FriendResponseDTO>builder()
                .data(friendService.update(request , id))
                .message("update thành công")
                .build();
    }

    @DeleteMapping("/deleteFriend")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public DataResponse<FriendResponseDTO> delete(@RequestParam int userId, @RequestParam int friendId){
        int id =  friendService.idFriend(userId, friendId);
        if(id == 0)
            return DataResponse.<FriendResponseDTO>builder()
                    .data(null)
                    .message("2 người không phải bạn bè")
                    .statusCode(400)
                    .build();
        return DataResponse.<FriendResponseDTO>builder()
                .data(friendService.delete(id))
                .message("xoá thành công")
                .statusCode(200)
                .build();
    }

    @GetMapping("/isFriend")
    public ResponseEntity<Map<String, Boolean>> checkFriendship(@RequestParam int userId, @RequestParam int friendId) {
        boolean isFriend = friendService.isFriend(userId ,  friendId);
        return ResponseEntity.ok(Map.of("isFriend", isFriend));
    }

}
