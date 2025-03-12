package com.example.social.media.controller;

import com.example.social.media.entity.MessageEmotion;
import com.example.social.media.mapper.ConversationMapper;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.ConversationDTO.CreateGroupConversationRequest;
import com.example.social.media.payload.request.MessageEmotion.MessageEmotionDTO;
import com.example.social.media.service.MessageEmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messageemotion")
public class MessageEmotionController {
    private MessageEmotionService messageEmotionService;
    @Autowired
    public MessageEmotionController(MessageEmotionService messageEmotionService){
        this.messageEmotionService = messageEmotionService;
    }
    @PostMapping("/create")
    public ResponseEntity<DataResponse> createMessageEmotion(
            @RequestHeader("User-Id") Integer creatorId, // header là id đang đăng nhập
            @RequestBody MessageEmotionDTO request){

        DataResponse dataResponse = new DataResponse();
        try {
            MessageEmotion me = messageEmotionService.createMessageEmotion(request.getMessageId(), request.getEmotion(), creatorId);
            dataResponse.setData("Id of messageEmotio: "+me.getMessageEmotionId());
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
}
