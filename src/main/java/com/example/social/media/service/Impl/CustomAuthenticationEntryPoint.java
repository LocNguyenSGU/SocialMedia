package com.example.social.media.service.Impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden

        Map<String, Object> data = new HashMap<>();
        data.put("error", "Forbidden");
        data.put("message", "You need to login to access this resource.");
        data.put("status", 403);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().write(objectMapper.writeValueAsBytes(data));
    }
}

