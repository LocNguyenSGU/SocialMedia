package com.example.social.media.controller;

import com.example.social.media.entity.User;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.common.PageResponse;
import com.example.social.media.payload.request.ProfileDTO.AvatarUpdateRequest;
import com.example.social.media.payload.request.ProfileDTO.UserUpdateRequest;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
import com.example.social.media.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    @GetMapping("/getdsusers")
    public ResponseEntity<PageResponse<UserResponse>> getDsUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserResponse> userPage = service.getDsUsers(page, size);

        return ResponseEntity.ok(new PageResponse<>(userPage));
    }


    @PostMapping("/active/{userId}")
    public ResponseEntity<DataResponse> updateActive(@PathVariable("userId") int userId , @RequestParam(defaultValue = "true") Boolean isActive){
        return  ResponseEntity.ok(new DataResponse(200 , service.updateActive(userId , isActive) , "update user success"));
    }

    @GetMapping("/getdsusersbykeyword")
    public ResponseEntity<PageResponse<UserResponse>> getDsUsersByKeyword(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserResponse> userPage = service.getDsUsersByKeyword(keyword, page, size);

        return ResponseEntity.ok(new PageResponse<>(userPage));
    }


    @GetMapping("/getinfouser/{userId}")
    public ResponseEntity<DataResponse> getinfouser(@PathVariable("userId") int userId){
        Optional<UserResponse> userResponse = Optional.ofNullable(service.getUserInfo(userId));
        if(userResponse.isPresent())
            return  ResponseEntity.ok(new DataResponse(200 , userResponse.get() , "Get info user success"));
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(404 , null ,"Not found"));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam(defaultValue = "0") String userId , @RequestBody UserUpdateRequest user) {
        System.out.println("user id : " + userId);
        Map<String, String> errors = service.validateUserFields(Integer.parseInt(userId),user.getUserName(), user.getEmail(), user.getPhoneNumber());

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok("Hợp lệ, có thể đăng ký.");
    }

    @GetMapping("/users/count")
    public ResponseEntity<DataResponse> getUserDetailsBetween(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        Map<String, Object> result = service.getUsersCreatedBetween(start, end);

        return ResponseEntity.ok(
                new DataResponse(200, result, "Thống kê người dùng thành công")
        );
    }
}
