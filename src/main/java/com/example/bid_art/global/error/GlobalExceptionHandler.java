package com.example.bid_art.global.error;

import com.example.bid_art.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청 (예: 현재가보다 낮은 입찰, 존재하지 않는 물품 등)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
    }

    /**
     * @Valid 검증 실패 (DTO 필드 제약 위반)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "입력값이 올바르지 않습니다.";
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        // 나중에 로그 라이브러리로 교체할 부분
        System.out.println("에러 발생: " + e.getMessage());
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("서버 내부 에러가 발생했습니다."));
    }
}
