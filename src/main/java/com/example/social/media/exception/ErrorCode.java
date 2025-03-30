package com.example.social.media.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1000 , "User not found" , HttpStatus.NOT_FOUND),
    MESSAGE_NOT_EXITED(1001 , "Message not found" , HttpStatus.NOT_FOUND),
    POST_NOT_EXISTED(8000 , "Post not found" , HttpStatus.NOT_FOUND),
    BAD_REQUEST_FAKE_NEWS(8002, "Post khong co content o dang chu, khong kiem tra duoc",HttpStatus.BAD_REQUEST)
    ;


    int code;
    String message;
    HttpStatusCode statusCode;
}
