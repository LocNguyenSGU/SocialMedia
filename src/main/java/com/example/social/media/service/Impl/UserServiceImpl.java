package com.example.social.media.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.UserMapper;
import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.social.media.controller.UserController.ALLOWED_CONTENT_TYPES;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Cloudinary cloudinary;


    private static final String UPLOAD_DIR = "uploads/avatars/";

    @Override
    public Optional<UserResponse> getUserProfile(int userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserResponse> updateUserProfile(int userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Kiểm tra username trùng (trừ chính user đang sửa)
        if (request.getUserName() != null && !user.getUserName().equals(request.getUserName()) &&
                userRepository.existsByUserName(request.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Kiểm tra email trùng (trừ chính user đang sửa)
        if (request.getEmail() != null && !user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Cập nhật thông tin user
        user.setUserName(request.getUserName() != null ? request.getUserName().trim() : user.getUserName());
        user.setFirstName(request.getFirstName() != null ? request.getFirstName().trim() : user.getFirstName());
        user.setLastName(request.getLastName() != null ? request.getLastName().trim() : user.getLastName());
        user.setEmail(request.getEmail() != null ? request.getEmail().trim() : user.getEmail());
        user.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : user.getPhoneNumber());

        userRepository.save(user);
        return Optional.of(userMapper.toDto(user));
    }



    @Override
    public Optional<UserResponse> updateUserAvatar(int userId, AvatarUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MultipartFile avatarFile = request.getAvatar();
        if (avatarFile == null || avatarFile.isEmpty()) {
            throw new IllegalArgumentException("Ảnh đại diện không được để trống.");
        }

        // Kiểm tra định dạng file
        String contentType = avatarFile.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Ảnh đại diện phải có định dạng JPEG, PNG hoặc GIF.");
        }

        try {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(avatarFile.getBytes(),
                    ObjectUtils.asMap("folder", "user_avatars"));

            // Lấy URL ảnh từ kết quả upload
            String imageUrl = (String) uploadResult.get("secure_url");

            // Cập nhật đường dẫn vào database
            user.setUrlAvatar(imageUrl);
            userRepository.save(user);

            return Optional.of(userMapper.toDto(user));

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tải ảnh lên Cloudinary: " + e.getMessage());
        }
    }



    @Override
    public boolean isUserNameExists(String userName) {
        return userRepository.existsByUserName(userName.trim());
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email.trim());
    }

    @Override
    public Page<UserResponse> getDsUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(userMapper::toDto);
    }


    @Override
    public UserResponse updateActive(int userId, Boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " User not exist"));
        user.setIsActive(isActive);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public Page<UserResponse> getDsUsersByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        Page<User> userPage = userRepository.searchUsers(keyword, pageable);

        return userPage.map(userMapper::toDto);
    }


    @Override
    public UserResponse getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " User not exist"));
        return userMapper.toDto(user);
    }

    @Override
    public Map<String, String> validateUserFields(Integer userId, String userName, String email, String phoneNumber) {
        Map<String, String> errors = new HashMap<>();

        if (userId == 0) {
            // Trường hợp tạo mới → kiểm tra toàn bộ
            if (userRepository.existsByUserName(userName)) {
                errors.put("userName", "Tên đăng nhập đã tồn tại.");
            }
            if (userRepository.existsByEmail(email)) {
                errors.put("email", "Email đã tồn tại.");
            }
            if (userRepository.existsByPhoneNumber(phoneNumber)) {
                errors.put("phoneNumber", "Số điện thoại đã tồn tại.");
            }
        } else {
            // Trường hợp cập nhật → bỏ qua chính userId hiện tại
            if (userRepository.existsByUserNameAndUserIdNot(userName, userId)) {
                errors.put("userName", "Tên đăng nhập đã tồn tại.");
            }
            if (userRepository.existsByEmailAndUserIdNot(email, userId)) {
                errors.put("email", "Email đã tồn tại.");
            }
            if (userRepository.existsByPhoneNumberAndUserIdNot(phoneNumber, userId)) {
                errors.put("phoneNumber", "Số điện thoại đã tồn tại.");
            }
        }

        return errors;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findByUserId(id);
    }
}

