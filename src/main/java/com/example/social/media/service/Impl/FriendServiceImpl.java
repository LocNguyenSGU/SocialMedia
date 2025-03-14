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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        friendRepository.save(friend);
        FriendResponseDTO response = friendMapper.toFriendResponseDTO(friend);
        return response;
    }

    @Override
    public FriendResponseDTO update(FriendUpdateRequest request, int id) {
        Friend friend = friendRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend not exist"));
        System.out.println("IS BLOCK : " + request.getIsBlock());
        friend.setIsBlock(request.getIsBlock());
        User user = userRepository.findById(request.getBlockByUser()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));
        friend.setBlockBy(user);
        friendRepository.save(friend);
        return friendMapper.toFriendResponseDTO(friend);
    }

    @Override
    public FriendResponseDTO delete(int id) {


        Friend friend = friendRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend not exist"));

        friendRepository.delete(friend);

        return friendMapper.toFriendResponseDTO(friend);
    }

    @Override
    public List<FriendResponseDTO> getDsFriends(int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " User not exist"));

        List<Friend> friends = friendRepository.findFriendsByUserId(userId);

        return friends.stream()
                .map(friendMapper::toFriendResponseDTO) // Dùng mapstruct để chuyển đổi
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendResponseDTO> getDsFriendsBlockByUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " User not exist"));

        List<Friend> friends = friendRepository.findBlockedFriendsByUserId(userId);

        return friends.stream()
                .map(friendMapper::toFriendResponseDTO) // Dùng mapstruct để chuyển đổi
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendResponseDTO> searchFriends(int userId , String keyword) {

        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));


        List<Friend> friends = friendRepository.searchFriends(userId , keyword);

        return friends.stream()
                .map(friendMapper::toFriendResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFriend(int userId, int friendId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend not exist"));

        return friendRepository.existsByUserIdAndFriendId(userId , friendId);
    }

}
