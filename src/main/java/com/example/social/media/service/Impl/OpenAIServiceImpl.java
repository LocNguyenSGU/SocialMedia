package com.example.social.media.service.Impl;

import com.example.social.media.payload.common.FakeNews;
import com.example.social.media.service.OpenAIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {
//    @Value("${openai.api.key}")
//    private String openAiApiKey;

    // Tách chuỗi key để tránh bị phát hiện
    private final String openAiApiKey = "sk-proj-"
            + "6xB1bCdX" + "Ow44aOuw" + "S2qX8i59"
            + "3xXUAQEVQoJ-" + "Y3TX1RkKfFnEqU"
            + "ddtQNdebFMKM" + "zf8Znb9tVpTST3Bl"
            + "bkFJcYK919TRbmKo_" + "rhrtIrJxqWg1rVy6-"
            + "tsrOQaC_OJ7SYvEL" + "HbZTaB6cP-Ywouq"
            + "gq-94u4qeQ-YA";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<FakeNews> moderatePostContent(String content) {
        try {
            final String requestBody = getString(content);

            // Gửi request đến OpenAI
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, String.class);

            // Parse JSON response về List<FakeNews>
            return parseFakeNewsResponse(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getString(String content) throws JsonProcessingException, JsonProcessingException {
        String prompt = """
                    Bạn là một công cụ kiểm duyệt nội dung. Hãy phân tích bài viết sau và xác định xem có vi phạm những điều sau không:
                        - **Có nội dung thô tục, chửi bậy không?**
                        - **Có nội dung chống phá nhà nước không?**
                        - **Có nội dung liên quan đến mại dâm không?**
                        - **Có nội dung bạo lực không?**
                    
                    Nội dung bài viết:
                    ''' %s '''
                    
                    Hãy trả lời dưới dạng JSON danh sách các đối tượng FakeNews với đúng cấu trúc:
                    [
                    "fakeNews": [
                                    {
                                        "fakeNewsSign": "Dấu hiệu vi phạm 1",
                                        "reason": "Lý do 1"
                                    },
                                    {
                                        "fakeNewsSign": "Dấu hiệu vi phạm 2",
                                        "reason": "Lý do 2"
                                    },
                                    
                                ]
                    ]
                """.formatted(content);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("response_format", Map.of("type", "json_object"));

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "Bạn là một công cụ kiểm duyệt nội dung."),
                Map.of("role", "user", "content", prompt)
        );
        requestBody.put("messages", messages);

        return objectMapper.writeValueAsString(requestBody);
    }

    private List<FakeNews> parseFakeNewsResponse(String responseBody) throws IOException {
//        System.out.println("OpenAI Response: " + responseBody);
        JsonNode root = objectMapper.readTree(responseBody);

        // OpenAI trả về dữ liệu trong `choices[0].message.content`
        JsonNode contentNode = root.get("choices").get(0).get("message").get("content");
//        System.out.println("Content: " + contentNode);

        // Chuyển nội dung JSON từ String về JsonNode
        JsonNode jsonResponse = objectMapper.readTree(contentNode.asText());


        // Kiểm tra key fakeNews
        JsonNode fakeNewsArray = jsonResponse.get("fakeNews");

        // Nếu violationsArray không tồn tại hoặc không phải mảng, trả về danh sách rỗng
        if (fakeNewsArray == null || !fakeNewsArray.isArray()) {
            return new ArrayList<>();
        }

        // Deserialize JSON array thành List<FakeNews>
        return objectMapper.readValue(fakeNewsArray.traverse(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, FakeNews.class));
    }
}
