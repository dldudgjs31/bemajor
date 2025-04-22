package com.hong.bemajor.common;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 데이터베이스 관련 SQL 오류 처리
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleDataAccessException(DataAccessException e) {
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            ApiResponse<String> response = ApiResponse.error("Duplicate data found in the database.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (message.contains("constraint violation")) {
            ApiResponse<String> response = ApiResponse.error("Data constraint violation occurred.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            ApiResponse<String> response = ApiResponse.error("Database error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 유효성 검사 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);
        ApiResponse<String> response = ApiResponse.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception e) {
        ApiResponse<String> response = ApiResponse.error("An unexpected error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
