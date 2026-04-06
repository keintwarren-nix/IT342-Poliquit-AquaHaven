package com.aquahaven.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorDetail error;
    private String timestamp;

    @Data
    public static class ErrorDetail {
        private String code;
        private String message;
        private Object details;

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
            this.details = null;
        }
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = true;
        res.data = data;
        res.error = null;
        res.timestamp = Instant.now().toString();
        return res;
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = false;
        res.data = null;
        res.error = new ErrorDetail(code, message);
        res.timestamp = Instant.now().toString();
        return res;
    }
}