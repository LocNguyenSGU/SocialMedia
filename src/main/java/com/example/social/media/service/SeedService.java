package com.example.social.media.service;

import com.example.social.media.entity.Comment;
import com.example.social.media.entity.Post;
import com.example.social.media.entity.Role;
import com.example.social.media.entity.User;
import com.example.social.media.enumm.PostTypeEnum;
import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.repository.PostRepository;
import com.example.social.media.repository.RoleRepository;
import com.example.social.media.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class SeedService {
    PostRepository postRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;
    Faker faker = new Faker();
    Random random = new Random();

    @Transactional
    public void clearSeeds(){
        userRepository.deleteAllInBatch();
        roleRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
    }

    @Transactional
    public void defaultSeeds(){
        Map<String , Role> roles =new HashMap<>();

        roles.put(
            "USER" ,
                Role.builder()
                        .name("USER")
                        .description("user only")
                        .build());

        roles.put(
                "ADMIN",
                Role.builder()
                        .name("ADMIN")
                        .description("admin only")
                        .build()
        );

        roles.forEach((key, role) -> {
            if (roleRepository.findById(role.getName()) == null){
                var roleSaved = roleRepository.save(role);
                roles.put(key , roleSaved);
            }
        });

        log.info("roles: {}" , roles);

        if (userRepository.findByUserName("ADMIN") == null) {
            userRepository.save(User.builder()
                    .userName("ADMIN")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .isActive(true)
                    .isOnline(false) // Giá trị mặc định là false
                    .createdAt(LocalDateTime.now()) // Đảm bảo trường createdAt có giá trị
                    .roles(new HashSet<>(roles.values())) // Gán danh sách roles
                    .build());
        }
    }

    @Transactional
    public void mockSeeds(){
        List<User> users = new ArrayList<>();
        List<Post> posts  = new ArrayList<>();

        for (int i = 0 ; i < 63 ; i++){
            User user = new User();
            user.setUserName(faker.name().username());
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode(faker.internet().password())); // Bạn có thể mã hóa nếu cần
            user.setPhoneNumber(faker.phoneNumber().cellPhone());
            user.setIsActive(faker.bool().bool());
            user.setIsOnline(random.nextBoolean());
            user.setLastActiveAt(LocalDateTime.now().minusHours(random.nextInt(48))); // Random trong 48h qua
            user.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30))); // Random trong 30 ngày qua
            user.setUrlAvatar(faker.internet().image());
            user.setUrlBackground(faker.internet().image());

            users.add(user);
        }
        userRepository.saveAll(users);

        for (var user : users){
            Post post = new Post();
                post.setUser(userRepository.findByUserName(user.getUserName())); // Gán post cho user ngẫu nhiên
                post.setContent(faker.lorem().sentence(10));
                post.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                post.setUpdatedAt(post.getCreatedAt().plusHours(random.nextInt(24)));
                post.setNumberEmotion(random.nextInt(500));
                post.setNumberComment(random.nextInt(200));
                post.setNumberShare(random.nextInt(100));

                // Random visibility
                PostVisibilityEnum[] visibilities = PostVisibilityEnum.values();
                post.setVisibility(visibilities[random.nextInt(visibilities.length)]);

                // Random typePost
                PostTypeEnum[] postTypes = PostTypeEnum.values();
                post.setTypePost(postTypes[random.nextInt(postTypes.length)]);

                posts.add(post);
        }

        postRepository.saveAll(posts);
    }



}
