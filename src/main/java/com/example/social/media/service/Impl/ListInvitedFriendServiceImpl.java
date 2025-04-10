package com.example.social.media.service.Impl;


import com.example.social.media.entity.Conversation;
import com.example.social.media.entity.ListInvitedFriend;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.ListInvitedFriendMapper;
import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendCreateRequest;
import com.example.social.media.payload.request.ListInvitedFriend.ListInvitedFriendUpdateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
import com.example.social.media.payload.response.ListInvitedFriendDTO.ListInvitedFriendResponseDTO;
import com.example.social.media.repository.ListInvitedFriendRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.ConversationService;
import com.example.social.media.service.FriendService;
import com.example.social.media.service.ListInvitedFriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Service
@RequiredArgsConstructor
public class ListInvitedFriendServiceImpl implements ListInvitedFriendService {

    ListInvitedFriendRepository listInvitedFriendRepository ;
    ListInvitedFriendMapper listInvitedFriendMapper ;
    UserRepository userRepository ;
    FriendService friendService;
    @Autowired
    private ConversationService conversationService;

    @Override
    public ListInvitedFriendResponseDTO create(ListInvitedFriendCreateRequest request) {

        User checksender = userRepository.findById(request.getSender())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not exist"));
        User checkreceiver = userRepository.findById(request.getReceiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not exist"));


        // check xem 1 trong 2 đã gửi lời mời kết bạn cho người kia chưa
        //  nếu rồi thì sẽ không the? gui loi moi ket ban nua
        Optional<ListInvitedFriend> checkRequestExist =  listInvitedFriendRepository.filterReceiverAndSenderExist(request.getReceiver() ,  request.getSender());
        if (checkRequestExist.isPresent())
            return null;
        checkRequestExist =  listInvitedFriendRepository.filterReceiverAndSenderExist(request.getSender() ,  request.getReceiver());
        if (checkRequestExist.isPresent())
            return null;

        var listInvitedFriend = listInvitedFriendMapper.toListInvitedFriend(request);
        Optional<User> sender = userRepository.findById(request.getSender());
        Optional<User> receiver = userRepository.findById(request.getReceiver());
        listInvitedFriend.setSender(sender.get());
        listInvitedFriend.setReceiver(receiver.get());
        listInvitedFriend =  listInvitedFriendRepository.save(listInvitedFriend);

        ListInvitedFriendResponseDTO response = listInvitedFriendMapper.toListInvitedFriendResponseDTO(listInvitedFriend);
        response.setSender(request.getSender());
        response.setReceiver(request.getReceiver());

        return  response;
    }

    @Override
    public ListInvitedFriendResponseDTO upadte(ListInvitedFriendUpdateRequest request, int id) {
        ListInvitedFriend listInvitedFriend = listInvitedFriendRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ListInvitedFriend not exist"));
//      listInvitedFriendMapper.updateListInvitedFriend(listInvitedFriend , request);
        listInvitedFriend.setStatus(request.getStatus());
        listInvitedFriend = listInvitedFriendRepository.save(listInvitedFriend);
        if(request.getStatus().name().equals("ACCEPT")){
            FriendCreateRequest friendCreateRequest =  new FriendCreateRequest();
            friendCreateRequest.setUser_id(listInvitedFriend.getSender().getUserId());
            friendCreateRequest.setFriend_id(listInvitedFriend.getReceiver().getUserId());
            FriendResponseDTO friendResponseDTO = friendService.create(friendCreateRequest);
            try{
                Conversation conversation = conversationService.createOneToOneConversation(
                        listInvitedFriend.getSender().getUserId(),
                        listInvitedFriend.getReceiver().getUserId()
                );
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(friendResponseDTO);
        }
        ListInvitedFriendResponseDTO response = listInvitedFriendMapper.toListInvitedFriendResponseDTO(listInvitedFriend);
        response.setSender(listInvitedFriend.getSender().getUserId());
        response.setReceiver(listInvitedFriend.getReceiver().getUserId());
        return  response;
    }

    // lay danh sach minh da gui loi moi ket ban
    @Override
    public List<ListInvitedFriendResponseDTO> getDsBySenderId(int senderId) {

        User checksender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not exist"));

        List<ListInvitedFriend> listInvitedFriends =  listInvitedFriendRepository.getListBySenderId(senderId).stream().toList();
        List<ListInvitedFriendResponseDTO> response = new ArrayList<>();
        for(ListInvitedFriend item :  listInvitedFriends){
            ListInvitedFriendResponseDTO temp = new ListInvitedFriendResponseDTO();
            temp.setSender(item.getSender().getUserId());
            temp.setReceiver(item.getReceiver().getUserId());
            temp.setStatus(item.getStatus());
            response.add(temp);
        }
        return response;
    }

    ///  lay danhh sach minh nhan moi ket ban
    @Override
    public List<ListInvitedFriendResponseDTO> getDsByReceiverId(int receiverId) {

        User checkreceiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not exist"));

        List<ListInvitedFriend> listInvitedFriends =  listInvitedFriendRepository.getListByReceiverId(receiverId).stream().toList();
        List<ListInvitedFriendResponseDTO> response = new ArrayList<>();
        for(ListInvitedFriend item :  listInvitedFriends){
            ListInvitedFriendResponseDTO temp = new ListInvitedFriendResponseDTO();
            temp.setSender(item.getSender().getUserId());
            temp.setReceiver(item.getReceiver().getUserId());
            temp.setStatus(item.getStatus());
            response.add(temp);
        }
        return response;
    }

    @Override
    public ListInvitedFriendResponseDTO filterReceiverAndSenderExist(int senderId, int receiverId) {
        ListInvitedFriend listInvitedFriend = listInvitedFriendRepository.filterReceiverAndSenderExist(senderId , receiverId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite not exist"));
        return listInvitedFriendMapper.toListInvitedFriendResponseDTO(listInvitedFriend);
    }

    @Override
    public int filterReceiverAndSender(int senderId, int receiverId){
        ListInvitedFriend listInvitedFriend = listInvitedFriendRepository.filterReceiverAndSender(senderId , receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite not exist"));
        return listInvitedFriend.getListInvitedFriendId();
    }
}
