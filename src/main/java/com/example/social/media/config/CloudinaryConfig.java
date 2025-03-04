package com.example.social.media.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzow04q4h",
                "api_key", "337221829521575",
                "api_secret", "p46g6Q1c-q5HB83P1bEMTjpqMis",
                "secure", true
        ));
    }
}

