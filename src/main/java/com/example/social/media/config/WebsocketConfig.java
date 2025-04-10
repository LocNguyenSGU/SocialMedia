package com.example.social.media.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 🔹 Thêm "/queue" để hỗ trợ tin nhắn riêng tư
        registry.setApplicationDestinationPrefixes("/app"); // 🔹 Prefix để client gửi tin nhắn đến server
        registry.setUserDestinationPrefix("/user");// Client gửi tin nhắn đến server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:5173") // ✅ Cho phép tất cả frontend kết nối
                .withSockJS(); // ✅ Bật hỗ trợ SockJS

        // 🔔 Endpoint riêng cho notification
        registry.addEndpoint("/ws-notification")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }
}

