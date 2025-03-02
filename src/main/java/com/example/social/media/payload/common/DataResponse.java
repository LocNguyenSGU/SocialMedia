package com.example.social.media.payload.common;

import org.springframework.http.HttpStatus;

public class DataResponse {
    private int statusCode = 200;
    private Object data;
    private String message;

    public DataResponse(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public DataResponse(int statusCode, Object data, String message) {
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
    }

    // Getters và Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
