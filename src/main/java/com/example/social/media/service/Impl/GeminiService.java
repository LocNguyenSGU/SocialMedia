package com.example.social.media.service.Impl;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;

@Service
public class GeminiService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";
    private static final String API_KEY = "abc"; // Thay bằng API Key của bạn

    public String genCaptionFromImage(MultipartFile file) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(GEMINI_API_URL + "?key=" + API_KEY);
            request.addHeader("Content-Type", "application/json");

            // Chuyển đổi ảnh thành chuỗi Base64
            String base64Image = convertImageToBase64(file);

            // Tạo JSON request
            String jsonRequest = createJsonRequest(base64Image);
            request.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));

            // Gửi request
            try (CloseableHttpResponse response = client.execute(request)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResponse = mapper.readTree(response.getEntity().getContent());

                // Lấy nội dung phản hồi từ Gemini
                return jsonResponse.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi khi gửi ảnh lên Gemini API!";
        }
    }

    private String convertImageToBase64(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String createJsonRequest(String base64Image) {
        return "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        {\n" +
                "          \"inlineData\": {\n" +
                "            \"mimeType\": \"" + getMimeType(base64Image) + "\",\n" +
                "            \"data\": \"" + base64Image + "\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"text\": \"Hãy viết 1 câu caption về hình ảnh này để tôi đăng lên mạng xã hội. Trả về 1 câu duy nhất, không viết gì thêm, câu có độ dài vừa phải, thể hiện tâm trạng dựa theo bức ảnh\"\n" + // Prompt, có thể thay đổi
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private String getMimeType(String base64Image) {
        // Simple logic to get mime type, improve if needed
        if (base64Image.startsWith("/9j/")) {
            return "image/jpeg";
        } else if (base64Image.startsWith("iVBORw0KGgoAAAANSUhEUg")) {
            return "image/png";
        } else {
            return "image/jpeg"; // Default to jpeg
        }
    }
}
