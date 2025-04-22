package com.hong.bemajor.common;

public class ApiResponse<T> {

    private String status;  // 성공/실패 상태
    private T data;         // 실제 데이터
    private String error;   // 에러 메시지 (실패 시)

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = "success";
        response.data = data;
        return response;
    }

    // 실패 응답 생성
    public static <T> ApiResponse<T> error(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = "error";
        response.error = error;
        return response;
    }

    // 기본 생성자, 게터/세터 생략 (Lombok 사용 가능)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}