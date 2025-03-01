package com.example.social.media.service.Impl;

import com.example.social.media.entity.*;
import com.example.social.media.enumm.MessageStatusEnum;
import com.example.social.media.mapper.MessageMapper;
import com.example.social.media.payload.request.MessageDTO.SendMessageRequest;
import com.example.social.media.payload.response.Conversation.ConversationDTO;
import com.example.social.media.payload.response.MessageDTO.MessageDTO;
import com.example.social.media.repository.*;
import com.example.social.media.service.MessageService;
import jakarta.transaction.Transactional;
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
    private ConversationMemberRepository conMemRepo;
    private MessageStatusRepository messageStatusRepository;
    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    @Autowired
    public MessageServiceImpl (MessageRepository mesRepo,
                               ConversationMemberRepository conMemRepo,
                               MessageStatusRepository messageStatusRepository,
                               UserRepository userRepository,
                               ConversationRepository conversationRepository){
        this.mesRepo = mesRepo;
        this.conMemRepo = conMemRepo;
        this.messageStatusRepository = messageStatusRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
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
        List<ConversationMember> conMemList = conMemRepo.getConversationMemberByIdUser(id);
        List<ConversationDTO> conversationDTOList = new ArrayList<>();
        for(ConversationMember c : conMemList){
            ConversationDTO conDto = new ConversationDTO();
            conDto.setFirstNameReceiver(c.getUser().getFirstName());
            conDto.setLastNameReceiver(c.getUser().getLastName());
            conversationDTOList.add(conDto);
        }
        if(!conversationDTOList.isEmpty()){
            return conversationDTOList;
        }
        return null;
    }

    // nếu có lỗi thì tất cả các thay đổi sql trước đó sẽ rollback
    @Transactional
    public Message sendMessage(SendMessageRequest request) {
        // Lấy người gửi từ DB
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy hội thoại từ DB
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Tạo tin nhắn mới
        Message message = new Message();
        message.setConversationId(conversation);
        message.setSender(sender);
        message.setTypeMessage(request.getTypeMessage());
        message.setContent(request.getContent());
        message.setCreatedAt(LocalDateTime.now());

        // Lưu tin nhắn vào DB
        message = mesRepo.save(message);

        // Lấy danh sách người tham gia cuộc trò chuyện
        List<ConversationMember> members = conMemRepo.findByConversationId(conversation.getConversationId());

        // Tạo trạng thái tin nhắn cho mỗi thành viên
        List<MessageStatus> messageStatuses = new ArrayList<>();
        for (ConversationMember member : members) {
            MessageStatus status = new MessageStatus();
            status.setMessage(message);
            status.setReceiver(member.getUser());
            status.setMessageStatus(MessageStatusEnum.SENT);
            messageStatuses.add(status);
        }

        // Lưu trạng thái tin nhắn
        messageStatusRepository.saveAll(messageStatuses);

        return message;
    }
}
