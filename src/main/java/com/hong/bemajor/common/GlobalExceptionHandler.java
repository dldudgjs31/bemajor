package com.hong.bemajor.common;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리 핸들러
 * - 각 예외 유형별로 적절한 HTTP 상태 코드와 메시지를 반환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 데이터베이스 관련 SQL 오류 처리
     * 응답 코드: 400 Bad Request 또는 500 Internal Server Error
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleDataAccessException(DataAccessException e) {
        String message = e.getMessage();
        ApiResponse<String> response;
        HttpStatus status;

        if (message.contains("Duplicate entry")) {
            response = ApiResponse.error("이미 등록된 데이터");
            status = HttpStatus.BAD_REQUEST;  // 400
        } else if (message.contains("constraint violation")) {
            response = ApiResponse.error("무결성 에러");
            status = HttpStatus.BAD_REQUEST;  // 400
        } else {
            response = ApiResponse.error("데이터 오류: " + message);
            status = HttpStatus.INTERNAL_SERVER_ERROR;  // 500
        }

        return ResponseEntity.status(status).body(response);
    }

    /**
     * 유효성 검사 실패 처리
     * 응답 코드: 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);
        ApiResponse<String> response = ApiResponse.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400
    }

    /**
     * 사용자 미발견 예외 처리
     * 응답 코드: 404 Not Found
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UsernameNotFoundException e) {
        ApiResponse<String> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 404
    }

    /**
     * 잘못된 자격증명(비밀번호 불일치) 처리
     * 응답 코드: 401 Unauthorized
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(BadCredentialsException e) {
        ApiResponse<String> response = ApiResponse.error("비밀번호가 일치하지 않습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 401
    }

    /**
     * 인증 실패(Authentication) 처리
     * 응답 코드: 401 Unauthorized
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException e) {
        ApiResponse<String> response = ApiResponse.error("인증 실패: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 401
    }

    /**
     * 권한 부족(Access Denied) 처리
     * 응답 코드: 403 Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException e) {
        ApiResponse<String> response = ApiResponse.error("접근 권한이 없습니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);  // 403
    }

    /**
     * CSRF 토큰 오류 처리
     * 응답 코드: 403 Forbidden
     */
    @ExceptionHandler(CsrfException.class)
    public ResponseEntity<ApiResponse<String>> handleCsrfException(CsrfException e) {
        ApiResponse<String> response = ApiResponse.error("CSRF token 오류: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);  // 403
    }

    /**
     * 잘못된 HTTP 메서드 처리
     * 응답 코드: 405 Method Not Allowed
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        ApiResponse<String> response = ApiResponse.error("허용되지 않는 HTTP 메서드입니다.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);  // 405
    }

    /**
     * 지원되지 않는 미디어 타입 처리
     * 응답 코드: 415 Unsupported Media Type
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        ApiResponse<String> response = ApiResponse.error("지원되지 않는 미디어 타입입니다.");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);  // 415
    }

    /**
     * HTTP 메시지 읽기 실패 처리 (JSON 파싱 오류 등)
     * 응답 코드: 400 Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleMessageNotReadable(HttpMessageNotReadableException e) {
        ApiResponse<String> response = ApiResponse.error("잘못된 요청 형식입니다: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400
    }

    /**
     * 필수 요청 파라미터 누락 처리
     * 응답 코드: 400 Bad Request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingParam(MissingServletRequestParameterException e) {
        ApiResponse<String> response = ApiResponse.error(e.getParameterName() + " 파라미터가 필요합니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400
    }

    /**
     * 파라미터 타입 불일치 처리
     * 응답 코드: 400 Bad Request
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatch(TypeMismatchException e) {
        ApiResponse<String> response = ApiResponse.error("잘못된 파라미터 타입입니다: " + e.getPropertyName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400
    }

    /**
     * 파일 업로드 용량 초과 처리
     * 응답 코드: 413 Payload Too Large
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        ApiResponse<String> response = ApiResponse.error("업로드 가능한 최대 파일 크기를 초과했습니다.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);  // 413
    }

    /**
     * 기타 예외 처리
     * 응답 코드: 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception e) {
        ApiResponse<String> response = ApiResponse.error("시스템 에러: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // 500
    }
}
