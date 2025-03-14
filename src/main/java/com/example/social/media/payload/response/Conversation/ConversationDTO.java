package com.example.social.media.payload.response.Conversation;

import com.example.social.media.payload.response.MessageDTO.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {

    private int idConversation;
    //inf of sender
    private String firstNameSender;
    private String lastNameSender;
    //inf of receiver
    private String firstNameReceiver;
    private String lastNameReceiver;

    private List<MessageDTO> listMessageDTO;
}
