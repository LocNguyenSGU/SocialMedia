package com.example.social.media.exception;

import com.example.social.media.payload.common.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<DataResponse> handlingAppException(AppException ex){
        log.error("Exception:" + ex);
        ErrorCode errorCode = ex.getErrorCode();
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatusCode(errorCode.getCode());
        dataResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(dataResponse);
    }
}
