package com.example.social.media.controller;

import com.example.social.media.entity.Message;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.MessageDTO.SendMessageRequest;
import com.example.social.media.payload.response.Conversation.ConversationDTO;
import com.example.social.media.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
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
            dataResponse.setMessage("get message having conversation id: " + idConversation + "successfully");
        } else {
            dataResponse.setStatusCode(404);
            dataResponse.setData(null);
            dataResponse.setMessage("get message unsuccessfully");
        }
        return new ResponseEntity<>(dataResponse, HttpStatus.valueOf(dataResponse.getStatusCode()));
    }
    @GetMapping("/getAllConversation/{idUser}")
    public ResponseEntity<DataResponse> getAllConversationByIdUser(@PathVariable int idUser){
        List<ConversationDTO> conDTO = messageService.getAllConversationByIdUser(idUser);
        DataResponse dataResponse = new DataResponse();
        if(conDTO != null){
            dataResponse.setData(conDTO);
            dataResponse.setMessage("get conversation by user id: " + idUser + "successfully");
        } else {
            dataResponse.setStatusCode(404);
            dataResponse.setData(null);
            dataResponse.setMessage("get conversation unsuccessfully");
        }
        return new ResponseEntity<>(dataResponse, HttpStatus.valueOf(dataResponse.getStatusCode()));
    }

    @PostMapping("/send")
    public ResponseEntity<DataResponse> sendMessage(@RequestBody SendMessageRequest request) {
        Message message = messageService.sendMessage(request);
        DataResponse dataResponse = new DataResponse();
        if(message != null){
            dataResponse.setData(message.getContent());
            dataResponse.setMessage("send message successfully");
        } else {
            dataResponse.setStatusCode(404);
            dataResponse.setData(null);
            dataResponse.setMessage("send message unsuccessfully");
        }
        return new ResponseEntity<>(dataResponse, HttpStatus.valueOf(dataResponse.getStatusCode()));
    }
    @PostMapping("/sendFile")
    public ResponseEntity<DataResponse> sendMessageHaveFile(@RequestPart("request") SendMessageRequest request, @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {
        Message message = messageService.sendMessageHaveFile(request, files);
        DataResponse dataResponse = new DataResponse();
        if(message != null){
            dataResponse.setData(message.getContent());
            dataResponse.setMessage("send message have file successfully");
        } else {
            dataResponse.setStatusCode(404);
            dataResponse.setData(null);
            dataResponse.setMessage("send message have file unsuccessfully");
        }
        return new ResponseEntity<>(dataResponse, HttpStatus.valueOf(dataResponse.getStatusCode()));
    }
}
