package com.example.social.media.payload.response.SeachResult;

import com.example.social.media.entity.Post;
import com.example.social.media.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class SearchResultResponse {
    List<User> users = new ArrayList<>();
    List<Post> posts = new ArrayList<>();

    public SearchResultResponse(User user) {
        users.add(user);
    }

    public SearchResultResponse(Post post) {
        posts.add(post);
    }
}
