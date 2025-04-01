package com.example.social.media.controller;

import com.example.social.media.entity.Conversation;
import com.example.social.media.mapper.ConversationMapper;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.ConversationDTO.CreateGroupConversationRequest;
import com.example.social.media.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.social.media.mapper.ConversationMapper.mapToDTO;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    private ConversationService conversationService;
    @Autowired
    public ConversationController(ConversationService conversationService){
        this.conversationService = conversationService;
    }
    //tạo nhóm chat
    @PostMapping("/group")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<DataResponse> createGroupConversation(
            @RequestHeader("User-Id") Integer creatorId, // header là id đang đăng nhập
            @RequestBody CreateGroupConversationRequest request) {
        DataResponse dataResponse = new DataResponse();
        try {
            // gọi hàm tạo conversation từ service
            Conversation conversation = conversationService.createNewConversation(
                    creatorId,
                    request.getParticipantIds(),
                    request.getName()
            );
            // response khi tạo ok
            dataResponse.setData(ConversationMapper.mapToDTO(conversation));
            dataResponse.setMessage("create successfully");
            dataResponse.setStatusCode(201);
            return new ResponseEntity<>(dataResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // tạo thất bại
            dataResponse.setData(null);
            dataResponse.setMessage("baka you created unsuccessfully");
            dataResponse.setStatusCode(400);
            return new ResponseEntity<>(dataResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // lỗi server
            dataResponse.setData(null);
            dataResponse.setMessage("baka server error");
            dataResponse.setStatusCode(500);
            return new ResponseEntity<>(dataResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //hàm tạo chat giữa 2 người
    @PostMapping("/one-to-one")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<DataResponse> createOneToOneConversation(
            @RequestHeader("User-Id") Integer creatorId,
            @RequestParam("participantId") Integer participantId) {
        DataResponse dataResponse = new DataResponse();
        try {
            Conversation conversation = conversationService.createOneToOneConversation(
                    creatorId,
                    participantId
            );
            dataResponse.setData(ConversationMapper.mapToDTO(conversation));
            dataResponse.setMessage("create successfully");
            dataResponse.setStatusCode(200);
            return new ResponseEntity<>(dataResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            dataResponse.setData(null);
            dataResponse.setMessage("baka you created unsuccessfully");
            dataResponse.setStatusCode(400);
            return new ResponseEntity<>(dataResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            dataResponse.setData(null);
            dataResponse.setMessage("baka server error");
            dataResponse.setStatusCode(500);
            return new ResponseEntity<>(dataResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // hàm lấy thông tin của 1 conversation bằng id
    @GetMapping("/{conversationId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<DataResponse> getConversation(
            @PathVariable("conversationId") Integer conversationId) {
        DataResponse dataResponse = new DataResponse();
        try {
            Conversation conversation = conversationService.getConversationById(conversationId);
            if (conversation == null) {
                dataResponse.setData(null);
                dataResponse.setMessage("baka not found");
                dataResponse.setStatusCode(404);
                return new ResponseEntity<>(dataResponse, HttpStatus.NOT_FOUND);
            }
            dataResponse.setData(ConversationMapper.mapToDTO(conversation));
            dataResponse.setMessage("create successfully");
            dataResponse.setStatusCode(200);
            return new ResponseEntity<>(dataResponse, HttpStatus.OK);
        } catch (Exception e) {
            dataResponse.setData(null);
            dataResponse.setMessage("baka server error");
            dataResponse.setStatusCode(500);
            return new ResponseEntity<>(dataResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
