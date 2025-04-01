package com.example.social.media.payload.response.SeachResult;

import com.example.social.media.payload.response.PostDTO.PostResponseDTO;
import com.example.social.media.payload.response.ProfileDTO.UserResponse;
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
    List<UserResponse> users = new ArrayList<>();
    List<PostResponseDTO> posts = new ArrayList<>();

    public SearchResultResponse(UserResponse user) {
        users.add(user);
    }

    public SearchResultResponse(PostResponseDTO post) {
        posts.add(post);
    }

}
