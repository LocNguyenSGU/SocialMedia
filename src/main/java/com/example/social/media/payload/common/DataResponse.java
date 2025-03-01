package com.example.social.media.payload.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataResponse<T> {
    @Builder.Default
    private int statusCode = 200;
    private T data;
    private String message;

    public DataResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
