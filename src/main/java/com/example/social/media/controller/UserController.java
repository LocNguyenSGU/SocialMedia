package com.example.social.media.controller;

import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import com.example.social.media.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService service;

    public static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResponse> getUserProfile(@PathVariable int userId) {
        Optional<UserResponse> userResponse = service.getUserProfile(userId);

        if (userResponse.isPresent()) {
            return ResponseEntity.ok(new DataResponse(200, userResponse.get(), "Get profile by ID success!!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse(404, null, "Profile not found!!"));
        }
    }


    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResponse> updateUserProfile(
            @PathVariable int userId,
            @Valid @RequestBody UserUpdateRequest request,
            BindingResult bindingResult) {

        Map<String, String> validationErrors = new HashMap<>();
        Map<String, String> businessErrors = new HashMap<>();

        // 1️⃣ Nếu có lỗi validation, thêm vào validationErrors
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    validationErrors.put(error.getField(), error.getDefaultMessage()));
        }

        try {
            // 2️⃣ Gọi service để cập nhật user, nếu có lỗi thì ném exception
            Optional<UserResponse> updatedUser = service.updateUserProfile(userId, request);



        } catch (IllegalArgumentException e) {
            // 4️⃣ Bắt lỗi từ UserServiceImpl (VD: username tồn tại, email tồn tại)
            businessErrors.put("businessError", e.getMessage());
        } catch (Exception e) {
            // 5️⃣ Bắt lỗi không mong muốn (nếu có)
            businessErrors.put("serverError", "Internal server error: " + e.getMessage());
        }

        // 6️⃣ Xử lý phản hồi dựa trên loại lỗi xảy ra

        if (!validationErrors.isEmpty() && !businessErrors.isEmpty()) {
            // 🔥 Nếu có cả lỗi validation và business
            Map<String, Object> allErrors = new HashMap<>();
            allErrors.put("validationErrors", validationErrors);
            allErrors.put("businessErrors", businessErrors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DataResponse(400, allErrors, "Validation and business logic errors."));
        }

        if (!validationErrors.isEmpty()) {
            // 🔥 Nếu chỉ có lỗi validation
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DataResponse(400, validationErrors, "Validation failed."));
        }

        if (!businessErrors.isEmpty()) {
            // 🔥 Nếu chỉ có lỗi business
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DataResponse(400, businessErrors, "Business logic error."));
        }

        // 🔥 Nếu không có lỗi nào, trả về thành công
        return ResponseEntity.ok(new DataResponse(200, null, "Profile updated successfully!"));
    }

    @PutMapping("/avatar/{userId}")
    public ResponseEntity<DataResponse> updateAvatar(@PathVariable int userId,
                                                     @ModelAttribute AvatarUpdateRequest request) {
        try {
            Optional<UserResponse> updatedUser = service.updateUserAvatar(userId, request);
            return ResponseEntity.ok(new DataResponse(200, updatedUser.get(), "Cập nhật ảnh đại diện thành công!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new DataResponse(400, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DataResponse(500, null, "Lỗi server: " + e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("xin chào");
    }
}
