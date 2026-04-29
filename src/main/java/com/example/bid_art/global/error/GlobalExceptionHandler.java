package com.example.bid_art.global.error;

import com.example.bid_art.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        // 나중에 로그 라이브러리로 교체할 부분
        System.out.println("에러 발생: " + e.getMessage()); 
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("서버 내부 에러가 발생했습니다."));
    }
}