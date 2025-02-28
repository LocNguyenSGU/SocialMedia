package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.response.Conversation.ConversationDTO;
import com.example.social.media.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }
    @GetMapping("/{idUser}/{idConversation}")
    public ResponseEntity<DataResponse> getMessageByidUser_idConversation(@PathVariable int idUser,
                                                                          @PathVariable int idConversation,
                                                                          @RequestParam(required = false) LocalDateTime lastMessageTime,
                                                                          @RequestParam(defaultValue = "20") int pageSize){
        ConversationDTO conDTO = messageService.getMessageByIdUser(idUser, idConversation, lastMessageTime, pageSize);
        DataResponse dataResponse = new DataResponse();
        if(conDTO != null){
            dataResponse.setData(conDTO);
            dataResponse.setMessage("get conversation id: " + idConversation + "successfully");
        } else {
            dataResponse.setStatusCode(404);
            dataResponse.setData(null);
            dataResponse.setMessage("get conversation unsuccessfully");
        }
        return new ResponseEntity<>(dataResponse, HttpStatus.valueOf(dataResponse.getStatusCode()));
    }
}
