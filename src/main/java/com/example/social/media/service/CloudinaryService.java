package com.example.social.media.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public interface CloudinaryService {
    Map<String, String> uploadFile(MultipartFile file) throws IOException;
    String deleteFile(String publicId) throws IOException;
}