package com.sparta.fmdelivery.exception;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.apipayload.BaseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(ApiException e) {

        BaseCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ApiResponse<String>> handleExceptionInternal(BaseCode errorCode) {

        return ResponseEntity.status(errorCode.getReasonHttpStatus().getHttpStatus())
                .body(ApiResponse.onFailure(errorCode));
    }
}
