package com.example.social.media.service.Impl;

import com.example.social.media.entity.Message;
import com.example.social.media.mapper.MessageMapper;
import com.example.social.media.payload.response.Conversation.ConversationDTO;
import com.example.social.media.payload.response.MessageDTO.MessageDTO;
import com.example.social.media.repository.ConversationMemberRepository;
import com.example.social.media.repository.ConversationRepository;
import com.example.social.media.repository.MessageRepository;
import com.example.social.media.repository.MessageStatusRepository;
import com.example.social.media.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository mesRepo;
    @Autowired
    public MessageServiceImpl (MessageRepository mesRepo){
        this.mesRepo = mesRepo;
    }
    @Override
    public ConversationDTO getMessageByIdUser(int idUser, int conversationId, LocalDateTime lastMessageTime, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Message> listMes = mesRepo.findMessageByConversationWithPagination(idUser, conversationId, lastMessageTime, pageable);
        List<MessageDTO> listMesDTO = new ArrayList<>();
        for(Message mes: listMes){
            listMesDTO.add(MessageMapper.mapToDTO(mes));
        }
        ConversationDTO conDTO = new ConversationDTO();
        conDTO.setListMessageDTO(listMesDTO);
        if (!listMes.isEmpty()) {
            conDTO.setFirstNameSender(listMes.get(0).getSender().getFirstName());
            conDTO.setLastNameSender(listMes.get(0).getSender().getLastName());
            if (!listMes.get(0).getMessageStatusList().isEmpty()) {
                conDTO.setFirstNameReceiver(listMes.get(0).getMessageStatusList().get(0).getReceiver().getFirstName());
                conDTO.setLastNameReceiver(listMes.get(0).getMessageStatusList().get(0).getReceiver().getLastName());
            }
        } else {
            return null;
        }
        return conDTO;
    }

    @Override
    public List<ConversationDTO> getAllConversationByIdUser(int id) {
        return null;
    }
}
