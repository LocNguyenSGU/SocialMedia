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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
    public Optional<UserResponse> removeUserAvatar(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Nếu avatar không phải mặc định, tiến hành xóa trên Cloudinary (nếu có)
        if (user.getUrlAvatar() != null && !user.getUrlAvatar().equals("/images/default-avatar.jpg")) {
            try {
                // Lấy public_id của ảnh từ URL (nếu cần xóa trên Cloudinary)
                String publicId = extractPublicIdFromUrl(user.getUrlAvatar());
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (Exception e) {
                throw new RuntimeException("Lỗi khi xóa avatar trên Cloudinary: " + e.getMessage());
            }
        }

        // Đặt avatar về ảnh mặc định
        user.setUrlAvatar("/images/default-avatar.jpg");
        userRepository.save(user);

        return Optional.of(userMapper.toDto(user));
    }

    // Phương thức trích xuất public_id từ URL của Cloudinary
    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        return parts[parts.length - 1].split("\\.")[0]; // Lấy phần không có đuôi .jpg, .png
    }



    @Override
    public List<Map<String, Object>> getNewUsersPerDay() {
        List<Object[]> results = userRepository.countNewUsersPerDay();
        return convertToMapList(results, "date", "count");
    }

    @Override
    public List<Map<String, Object>> getNewUsersPerMonth() {
        List<Object[]> results = userRepository.countNewUsersPerMonth();
        return convertToMapList(results, "year", "month", "count");
    }

    @Override
    public List<Map<String, Object>> getNewUsersPerYear() {
        List<Object[]> results = userRepository.countNewUsersPerYear();
        return convertToMapList(results, "year", "count");
    }

    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String... keys) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> data = new HashMap<>();
            for (int i = 0; i < keys.length; i++) {
                data.put(keys[i], row[i]);
            }
            dataList.add(data);
        }
        return dataList;
    }

}

