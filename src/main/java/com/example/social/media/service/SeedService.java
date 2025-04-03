package com.example.social.media.service;

import com.example.social.media.entity.*;
import com.example.social.media.enumm.CommentEmotionEnum;
import com.example.social.media.enumm.CommentTypeEnum;
import com.example.social.media.enumm.PostTypeEnum;
import com.example.social.media.enumm.PostVisibilityEnum;
import com.example.social.media.repository.*;
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
    CommentRepository commentRepository;
    CommentEmotionRepository commentEmotionRepository;
    CommentCloserRepository commentCloserRepository;

    PasswordEncoder passwordEncoder;
    Faker faker = new Faker();
    Random random = new Random();

    @Transactional
    public void clearSeeds(){
        userRepository.deleteAllInBatch();
        roleRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        commentCloserRepository.deleteAllInBatch();
        commentEmotionRepository.deleteAllInBatch();
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

        Map<String, Role> savedRoles = new HashMap<>();
        for (Map.Entry<String, Role> entry : roles.entrySet()) {
            if (!roleRepository.findById(entry.getValue().getName()).isPresent()) {
                Role savedRole = roleRepository.save(entry.getValue());
                savedRoles.put(entry.getKey(), savedRole);
            } else {
                savedRoles.put(entry.getKey(), entry.getValue());
            }
        }


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
            user.setPassword(passwordEncoder.encode("123456")); // Bạn có thể mã hóa nếu cần
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


        List<Comment> comments = new ArrayList<>();
        List<CommentEmotion> allCommentEmotions = new ArrayList<>(); // Để lưu tất cả CommentEmotion
        Random random = new Random();

// Giả sử CommentEmotionEnum đã được định nghĩa (ví dụ: LIKE, LOVE, HAHA, etc.)
        CommentEmotionEnum[] emotions = CommentEmotionEnum.values();

        for (int i = 0; i < 100; i++) { // Tạo 100 bình luận
            Comment comment = new Comment();

            Post randomPost = posts.get(random.nextInt(posts.size()));
            comment.setPost(randomPost);

            User randomUser = users.get(random.nextInt(users.size()));
            comment.setUser(randomUser);

            comment.setContent(faker.lorem().sentence(random.nextInt(15) + 5));

            CommentTypeEnum[] commentTypes = CommentTypeEnum.values();
            comment.setTypeComment(commentTypes[random.nextInt(commentTypes.length)]);

            comment.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            comment.setUpdatedAt(comment.getCreatedAt().plusHours(random.nextInt(24)));

            List<CommentEmotion> commentEmotions = new ArrayList<>();
            int emotionCount = random.nextInt(11);
            for (int j = 0; j < emotionCount; j++) {
                CommentEmotion emotion = new CommentEmotion();
                emotion.setComment(comment);
                emotion.setUser(users.get(random.nextInt(users.size())));
                emotion.setEmotion(emotions[random.nextInt(emotions.length)]);
                emotion.setCreatedAt(comment.getCreatedAt().plusMinutes(random.nextInt(1440)));
                commentEmotions.add(emotion);
                allCommentEmotions.add(emotion);
            }

            comment.setCommentEmotionList(commentEmotions);
            comment.setNumberEmotion(emotionCount);

            comment.setNumberCommentChild(random.nextInt(10));
            comment.setCommentMediaList(new ArrayList<>());
            comment.setAncestors(new ArrayList<>());
            comment.setDescendants(new ArrayList<>());

            comments.add(comment);
        }

        commentRepository.saveAll(comments);
        commentEmotionRepository.saveAll(allCommentEmotions);

        List<CommentCloser> commentClosers = new ArrayList<>();

        for (Comment comment : comments) {
            int maxChildren = Math.min(comment.getNumberCommentChild(), comments.size() - 1); // Tránh vượt quá số comment hiện có
            int childCount = random.nextInt(maxChildren + 1); // Số con từ 0 đến maxChildren

            Set<Integer> usedIndices = new HashSet<>();
            usedIndices.add(comments.indexOf(comment));

            for (int i = 0; i < childCount; i++) {
                int descendantIndex;
                do {
                    descendantIndex = random.nextInt(comments.size());
                } while (usedIndices.contains(descendantIndex));
                usedIndices.add(descendantIndex);

                Comment descendant = comments.get(descendantIndex);

                // Tạo CommentCloser
                CommentCloser commentCloser = new CommentCloser();
                commentCloser.setAncestor(comment);
                commentCloser.setDescendant(descendant);
                commentCloser.setDepth(1);
                commentCloser.setIdCommentCloser(i + 1);

                commentClosers.add(commentCloser);

                if (comment.getAncestors() == null) comment.setAncestors(new ArrayList<>());
                if (descendant.getDescendants() == null) descendant.setDescendants(new ArrayList<>());

                comment.getAncestors().add(commentCloser);
                descendant.getDescendants().add(commentCloser);
            }
        }

        for (int i = 0; i < comments.size() / 2; i++) { // Tạo thêm một số quan hệ cháu/chắt
            Comment ancestor = comments.get(random.nextInt(comments.size()));
            Comment middle = comments.get(random.nextInt(comments.size()));
            Comment descendant = comments.get(random.nextInt(comments.size()));

            if (ancestor != middle && middle != descendant && ancestor != descendant) {
                CommentCloser closer1 = new CommentCloser();
                closer1.setAncestor(ancestor);
                closer1.setDescendant(middle);
                closer1.setDepth(1);
                closer1.setIdCommentCloser(1);

                CommentCloser closer2 = new CommentCloser();
                closer2.setAncestor(middle);
                closer2.setDescendant(descendant);
                closer2.setDepth(1);
                closer2.setIdCommentCloser(1);

                CommentCloser closer3 = new CommentCloser();
                closer3.setAncestor(ancestor);
                closer3.setDescendant(descendant);
                closer3.setDepth(2);
                closer3.setIdCommentCloser(2);

                commentClosers.add(closer1);
                commentClosers.add(closer2);
                commentClosers.add(closer3);

                ancestor.getAncestors().add(closer1);
                middle.getDescendants().add(closer1);
                middle.getAncestors().add(closer2);
                descendant.getDescendants().add(closer2);
                ancestor.getAncestors().add(closer3);
                descendant.getDescendants().add(closer3);
            }
        }

        commentCloserRepository.saveAll(commentClosers);

    }



}
