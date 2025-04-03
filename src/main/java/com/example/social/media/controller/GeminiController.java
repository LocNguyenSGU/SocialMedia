package com.example.social.media.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/chatAi")
public class GeminiController {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chatGemini")
    public ResponseEntity<String> chatGemini(@RequestBody Map<String, Object> requestBody) {
        // Tạo Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Tạo Request
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Gửi yêu cầu HTTP POST
        ResponseEntity<String> response = restTemplate.exchange(geminiApiUrl, HttpMethod.POST, request, String.class);

        // Trả về Response từ API Gemini
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}


