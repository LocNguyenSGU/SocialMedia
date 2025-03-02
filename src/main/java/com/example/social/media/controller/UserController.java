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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService service;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @GetMapping("/{userId}")
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
    public ResponseEntity<DataResponse> updateUserProfile(
            @PathVariable int userId,
            @Valid @RequestBody UserUpdateRequest request,
            BindingResult bindingResult) {

        // Handle validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()) // Lấy thông báo lỗi cụ thể từ validation
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DataResponse(400, null, "Validation failed for the provided data."));
        }

//        // Kiểm tra username hoặc email trùng lặp
//        if (service.isUserNameExists(request.getUserName())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new DataResponse(404, null, "Username already exists"));
//        }
//        if (service.isEmailExists(request.getEmail())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new DataResponse(404, null, "Email already exists"));
//        }


        Optional<UserResponse> updatedUser = service.updateUserProfile(userId, request);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(new DataResponse(200, updatedUser.get(), "Profile updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse(404, null, "User not found!"));
        }
    }




    @PutMapping("/avatar/{userId}")
    public ResponseEntity<String> updateAvatar(@ModelAttribute AvatarUpdateRequest request) {
        MultipartFile avatar = request.getAvatar();

        if (avatar == null || avatar.isEmpty()) {

            return ResponseEntity.badRequest().body("Ảnh đại diện không được để trống.");
        }

        // Kiểm tra định dạng file
        String contentType = avatar.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("Ảnh đại diện phải có định dạng JPEG, PNG hoặc GIF.");
        }

        // TODO: Xử lý lưu ảnh (ví dụ: lưu vào thư mục hoặc cloud storage)

        return ResponseEntity.ok("Cập nhật ảnh đại diện thành công!");
    }

    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("xin chào");
    }
}
