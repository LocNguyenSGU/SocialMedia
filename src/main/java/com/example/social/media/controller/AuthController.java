package com.example.social.media.controller;
import com.example.social.media.entity.Role;
import com.example.social.media.entity.User;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.request.AuthDTO.AuthRequest;
import com.example.social.media.payload.request.AuthDTO.LogoutRequest;
import com.example.social.media.payload.request.AuthDTO.RefreshRequest;
import com.example.social.media.payload.response.Aut.AuthenticationResponse;
import com.example.social.media.repository.RoleRepository;
import com.example.social.media.repository.UserRepository;
import com.example.social.media.service.Impl.JwtService;
import com.example.social.media.service.Impl.UserInfoService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

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

    @Autowired
    private RoleRepository roleRepository;

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
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            errors.put("email", "Email already exists");
        }

        // Nếu có lỗi liên quan đến unique constraint, trả về luôn
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new DataResponse(400, errors, "Validation failed"));
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getByName("USER"));
        userInfo.setRoles(roles);

        boolean rs = service.addUser(userInfo);
        if (!rs) {
            return ResponseEntity.badRequest().body(new DataResponse(500, null, "There was an error!"));
        }

        return ResponseEntity.ok(new DataResponse(200, userInfo, "User Added Successfully"));
    }

    private String saveAvatar(byte[] image, String username) {
        try {
            String fileName = UUID.randomUUID() + "_" + username + ".png";
            String filePath = "avatars/" + fileName;
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), image);
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu avatar", e);
        }
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

    @PostMapping("/logout")
    public DataResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        jwtService.logout(logoutRequest);
        return DataResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public AuthenticationResponse authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = jwtService.refreshToken(request);
        return AuthenticationResponse.builder()
                .token(result)
                .authenticated(true)
                .build();
    }

    @PostMapping("/generateToken")
    public AuthenticationResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        User user = userRepository.findByUserName(authRequest.getUsername());

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = service.loadUserByUsername(authRequest.getUsername());


            return  new AuthenticationResponse().builder()
                     .authenticated(true)
                     .token(jwtService.generateToken(userDetails))
                     .userId(user.getUserId())
                     .build();

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
