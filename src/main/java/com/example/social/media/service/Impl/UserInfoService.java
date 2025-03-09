package com.example.social.media.service.Impl;

import com.example.social.media.entity.User;
import com.example.social.media.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                .roles("ADMIN") // Convert role if needed
                .build();
    }

    public boolean addUser(User userInfo) {
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        User u = repository.save(userInfo);
        return !u.getUserName().isEmpty();
    }
}
