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
}
