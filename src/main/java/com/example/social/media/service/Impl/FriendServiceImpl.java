package com.example.social.media.service.Impl;


import com.example.social.media.entity.Friend;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.FriendMapper;
import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
import com.example.social.media.repository.FriendRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Service
@RequiredArgsConstructor

public class FriendServiceImpl implements FriendService {

    FriendRepository friendRepository;
    FriendMapper friendMapper;
    UserRepository userRepository;


    @Override
    public FriendResponseDTO create(FriendCreateRequest request) {
        Friend friend = friendMapper.toFriend(request);
        Optional<User> user = userRepository.findById(request.getUser_id());
        Optional<User> friend_user =  userRepository.findById(request.getFriend_id());
        friend.setUser(user.get());
        friend.setFriend(friend_user.get());
        friendRepository.save(friend);
        FriendResponseDTO response = new FriendResponseDTO();
        response.setUser_id(request.getUser_id());
        response.setFriend_id(request.getFriend_id());
        return response;
    }
}
