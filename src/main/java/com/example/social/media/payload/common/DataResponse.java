package com.example.social.media.payload.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class DataResponse {
    private int statusCode = 200;
    private Object data;
    private String message;

    public DataResponse(Object data, String message) {
        this.data = data;
        this.message = message;
    }
}
