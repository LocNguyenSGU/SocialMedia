package com.example.social.media.service.Impl;

import com.example.social.media.entity.Role;
import com.example.social.media.entity.User;
import com.example.social.media.exception.AppException;
import com.example.social.media.exception.ErrorCode;
import com.example.social.media.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUserName(username);

        // Convert User entity to UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName()) // Ensure you use the correct getter method
                .password(user.getPassword()) // Ensure the password is encoded
//              .roles("ADMIN") // Convert role if needed
                .authorities(mapRolesToAuthorities(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public boolean addUser(User userInfo) {

        if (userInfo == null || userInfo.getUserName() == null || userInfo.getPassword() == null) {
            throw new AppException(ErrorCode.INVALID_USER_DATA);
        }

        if (repository.existsByUserName(userInfo.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return true;
    }
}
