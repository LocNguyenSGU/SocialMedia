package com.example.social.media.controller;

import com.example.social.media.entity.Role;
import com.example.social.media.entity.User;
import com.example.social.media.mapper.RoleMapper;
import com.example.social.media.mapper.UserMapper;
import com.example.social.media.payload.common.DataResponse;
import com.example.social.media.payload.response.AdminDTO.RoleDTO;
import com.example.social.media.payload.response.AdminDTO.UserRole;
import com.example.social.media.repository.RoleRepository;
import com.example.social.media.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AdminController {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    RoleMapper mapper;
    private final RoleMapper roleMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserRole> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/roles")
    public DataResponse<RoleDTO> createRole(@RequestBody RoleDTO response){

        if (roleRepository.findById(response.getName()).isPresent()){
            throw new RuntimeException("Role is existed");
        }

        Role role = new Role(response.getName() , response.getDescription());
        roleRepository.save(role);
        return DataResponse.<RoleDTO>builder()
                .data(response)
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/roles")
    public DataResponse<List<RoleDTO>> roleList(){
        return DataResponse.<List<RoleDTO>>builder()
                .data(roleRepository.findAll().stream().map(roleMapper::toRoleDTO).toList())
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/assign-roles/{userId}")
    public ResponseEntity<UserRole> assignRoles(@PathVariable Integer userId,
                                              @RequestBody Set<Role> roles) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }
}
