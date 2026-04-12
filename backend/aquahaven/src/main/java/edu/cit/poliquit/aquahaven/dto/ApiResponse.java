package edu.cit.poliquit.aquahaven.dto;

import java.time.Instant;

/**
 * Generic API envelope — wraps all controller responses.
 * Matches the existing ApiResponse<T> shape already in the project
 * but moved to the correct package (edu.cit.poliquit...).
 *
 * DESIGN PATTERN — Builder (Creational)
 * Consistent with AuthResponse.Builder already implemented.
 */
public class ApiResponse<T> {

    private final boolean success;
    private final T       data;
    private final Error   error;
    private final String  timestamp;

    private ApiResponse(Builder<T> b) {
        this.success   = b.success;
        this.data      = b.data;
        this.error     = b.error;
        this.timestamp = Instant.now().toString();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new Builder<T>().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new Builder<T>().success(false).error(new Error(code, message)).build();
    }

    public boolean isSuccess()  { return success; }
    public T       getData()    { return data; }
    public Error   getError()   { return error; }
    public String  getTimestamp(){ return timestamp; }

    // ── Nested Error ────────────────────────────────────────────────────────────
    public static class Error {
        private final String code;
        private final String message;
        public Error(String code, String message) { this.code = code; this.message = message; }
        public String getCode()    { return code; }
        public String getMessage() { return message; }
    }

    // ── Builder ─────────────────────────────────────────────────────────────────
    public static class Builder<T> {
        private boolean success;
        private T       data;
        private Error   error;

        public Builder<T> success(boolean v) { success = v; return this; }
        public Builder<T> data(T v)          { data    = v; return this; }
        public Builder<T> error(Error v)     { error   = v; return this; }
        public ApiResponse<T> build()        { return new ApiResponse<>(this); }
    }
}