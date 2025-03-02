package com.example.social.media.service.Impl;

import com.example.social.media.entity.User;
import com.example.social.media.mapper.UserMapper;
import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private static final String UPLOAD_DIR = "uploads/avatars/";

    @Override
    public Optional<UserResponse> getUserProfile(int userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserResponse> updateUserProfile(int userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra username trùng (trừ chính user đang sửa)
        if (!user.getUserName().equals(request.getUserName()) &&
                userRepository.existsByUserName(request.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Kiểm tra email trùng (trừ chính user đang sửa)
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setUserName(request.getUserName().trim());
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(request.getEmail().trim());
        user.setPhoneNumber(request.getPhoneNumber().trim());

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
        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Ảnh đại diện phải có định dạng hình ảnh.");
        }

        try {
            // Tạo thư mục lưu trữ nếu chưa tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Lưu file vào thư mục
            String fileName = StringUtils.cleanPath(userId + "_" + avatarFile.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật đường dẫn vào database (chỉ lưu đường dẫn, không lưu file vào DB)
            user.setUrlAvatar("/" + UPLOAD_DIR + fileName);
            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu ảnh đại diện: " + e.getMessage());
        }

        return Optional.of(userMapper.toDto(user));
    }


@Override
    public boolean isUserNameExists(String userName) {
        return userRepository.existsByUserName(userName.trim());
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email.trim());
    }
}

