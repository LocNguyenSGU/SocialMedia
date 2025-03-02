package com.example.social.media.payload.request.ProfileDTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AvatarUpdateRequest {
    private MultipartFile avatar;
}

