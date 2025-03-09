package com.example.social.media.controller;
import com.example.social.media.entity.User;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.AuthDTO.AuthRequest;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.Impl.JwtService;
import com.example.social.media.service.Impl.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<DataResponse> addNewUser(@RequestBody User userInfo) {
        Map<String, String> errors = validateUserInput(userInfo);

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUserName(userInfo.getUserName()) != null) {
            errors.put("userName", "Username already exists");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(userInfo.getEmail()).isPresent()) {
            errors.put("email", "Email already exists");
        }

        // Nếu có lỗi liên quan đến unique constraint, trả về luôn
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new DataResponse(400, errors, "Validation failed"));
        }

        boolean rs = service.addUser(userInfo);
        if (!rs) {
            return ResponseEntity.badRequest().body(new DataResponse(500, null, "There was an error!"));
        }

        return ResponseEntity.ok(new DataResponse(200, userInfo, "User Added Successfully"));
    }

    private Map<String, String> validateUserInput(User user) {
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(user.getUserName()) || user.getUserName().length() < 3 || user.getUserName().length() > 50) {
            errors.put("userName", "Username must be between 3 and 50 characters");
        }

        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Invalid email format");
        }

        if (!StringUtils.hasText(user.getPassword()) || user.getPassword().length() < 6) {
            errors.put("password", "Password must be at least 6 characters");
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            errors.put("phoneNumber", "Invalid phone number format");
        }

        return errors;
    }


    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
