package com.example.social.media.service.Impl;


import com.example.social.media.entity.Friend;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.FriendMapper;
import com.example.social.media.payload.request.FriendDTO.FriendCreateRequest;
import com.example.social.media.payload.request.FriendDTO.FriendUpdateRequest;
import com.example.social.media.payload.response.FriendDTO.FriendResponseDTO;
import com.example.social.media.repository.FriendRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        FriendResponseDTO response = friendMapper.toFriendResponseDTO(friend);
        return response;
    }

    @Override
    public FriendResponseDTO update(FriendUpdateRequest request, int id) {
        Friend friend = friendRepository.findById(id).orElseThrow(() -> new RuntimeException("Friend not exist"));
        friend.setIsBlock(request.isBlock());
        User user = userRepository.findById(request.getBlockByUser()).orElseThrow(() -> new RuntimeException("User not exist"));
        friend.setBlockBy(user);
        friendRepository.save(friend);
        return friendMapper.toFriendResponseDTO(friend);
    }

    @Override
    public FriendResponseDTO delete(int id) {
        Friend friend = friendRepository.findById(id).orElseThrow(() -> new RuntimeException("Friend not exist"));
        friendRepository.delete(friend);

        return friendMapper.toFriendResponseDTO(friend);
    }

    @Override
    public List<FriendResponseDTO> getDsFriends(int userId) {
        List<Friend> friends = friendRepository.findFriendsByUserId(userId);

        return friends.stream()
                .map(friendMapper::toFriendResponseDTO) // Dùng mapstruct để chuyển đổi
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendResponseDTO> searchFriends(int userId , String keyword) {
        List<Friend> friends = friendRepository.searchFriends(userId , keyword);

        return friends.stream()
                .map(friendMapper::toFriendResponseDTO)
                .collect(Collectors.toList());
    }

}
