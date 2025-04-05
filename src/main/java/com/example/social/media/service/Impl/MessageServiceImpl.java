package com.example.social.media.service.Impl;

import com.example.social.media.entity.*;
import com.example.social.media.enumm.MediaTypeEnum;
import com.example.social.media.enumm.MessageStatusEnum;
import com.example.social.media.mapper.MessageMapper;
import com.example.social.media.payload.request.MessageDTO.SendMessageRequest;
import com.example.social.media.payload.response.Conversation.ConversationDTO;
import com.example.social.media.payload.response.MessageDTO.MessageDTO;
import com.example.social.media.repository.*;
import com.example.social.media.service.CloudinaryService;
import com.example.social.media.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository mesRepo;
    private ConversationMemberRepository conMemRepo;
    private MessageStatusRepository messageStatusRepository;
    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    private CloudinaryService cloudinaryService;
    private MessageMediaRepository messageMediaRepository;
    @Autowired
    public MessageServiceImpl (MessageRepository mesRepo,
                               ConversationMemberRepository conMemRepo,
                               MessageStatusRepository messageStatusRepository,
                               UserRepository userRepository,
                               ConversationRepository conversationRepository,
                               CloudinaryService cloudinaryService,
                               MessageMediaRepository messageMediaRepository){
        this.mesRepo = mesRepo;
        this.conMemRepo = conMemRepo;
        this.messageStatusRepository = messageStatusRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.cloudinaryService = cloudinaryService;
        this.messageMediaRepository = messageMediaRepository;
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
            conDto.setIdConversation(c.getConversation().getConversationId());
            conDto.setFirstNameReceiver(c.getUser().getFirstName());
            conDto.setLastNameReceiver(c.getUser().getLastName());
            conDto.setIdUserReceive(c.getUser().getUserId());
            conversationDTOList.add(conDto);
        }
        if(!conversationDTOList.isEmpty()){
            return conversationDTOList;
        }
        return null;
    }

    // nếu có lỗi thì tất cả các thay đổi sql trước đó sẽ rollback
    @Override
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

    @Override
    @Transactional
    public Message sendMessageHaveFile(SendMessageRequest request, MultipartFile[] files) throws IOException {
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
        message.setContent((files != null && files.length > 0) ? "Đã gửi một file đính kèm" : request.getContent());
        message.setCreatedAt(LocalDateTime.now());

        // Lưu tin nhắn trước khi xử lý file
        message = mesRepo.save(message);

        // Xử lý danh sách file
        if (files != null && files.length > 0) {
            int order = 1; // Order cho từng file
            List<MessageMedia> mediaList = new ArrayList<>();
            for (MultipartFile file : files) {
                Map<String, String> uploadResult = cloudinaryService.uploadFile(file);

                // Kiểm tra URL hợp lệ
                String mediaUrl = uploadResult.get("url");
                if (mediaUrl == null || mediaUrl.isEmpty()) {
                    throw new IOException("File upload failed");
                }

                // Xác định loại file
                String mediaType = Optional.ofNullable(uploadResult.get("type")).orElse("").toLowerCase();
                MediaTypeEnum typeEnum = mediaType.startsWith("image") ? MediaTypeEnum.IMAGE : MediaTypeEnum.VIDEO;

                // Tạo MessageMedia
                MessageMedia messageMedia = new MessageMedia();
                messageMedia.setMessage(message);
                messageMedia.setMediaType(typeEnum);
                messageMedia.setCreatedAt(LocalDateTime.now());
                messageMedia.setMediaUrl(mediaUrl);
                messageMedia.setOrder(order++);
                mediaList.add(messageMedia);
            }
            messageMediaRepository.saveAll(mediaList); // Lưu tất cả file 1 lần
        }

        // Lấy danh sách người tham gia cuộc trò chuyện
        List<ConversationMember> members = conMemRepo.findByConversationId(conversation.getConversationId());

        // Tạo trạng thái tin nhắn
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
