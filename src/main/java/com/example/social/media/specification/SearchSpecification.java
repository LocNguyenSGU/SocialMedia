package com.example.social.media.specification;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SearchSpecification {

    public static Specification<User> hasUsername(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword != null) {

                return criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("userName")), "%" + keyword.trim().toLowerCase() + "%");
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Post> hasContentPost(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword != null) {
                return criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("content")), "%" + keyword.trim().toLowerCase() + "%");
            }
            return criteriaBuilder.conjunction();
        };
    }


}
