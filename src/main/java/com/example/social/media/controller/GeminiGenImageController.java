package com.example.social.media.controller;
import com.example.social.media.service.Impl.GeminiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/geminiGenImage")
public class GeminiGenImageController {
    @Autowired
    private GeminiService geminiService;

    @PostMapping("/generate-caption")
    public ResponseEntity<String> generateCaption(@RequestParam("file") MultipartFile file) {
        String jsonResponse = geminiService.genCaptionFromImage(file);
        String extractedText = extractTextDirectly(jsonResponse); // Gọi hàm trích xuất trực tiếp
        return ResponseEntity.ok(extractedText);
    }
    private String extractTextDirectly(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            return rootNode.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi khi xử lý JSON.";
        } catch (NullPointerException e) {
            return "Không tìm thấy text trong phản hồi.";
        }
    }
}
