package edu.cit.poliquit.aquahaven.dto;

import java.time.Instant;

public class AuthResponse {

    private boolean success;
    private Data data;
    private ErrorInfo error;
    private String timestamp;

    public AuthResponse() {
        this.timestamp = Instant.now().toString();
    }

    public static AuthResponse ok(UserInfo user, String accessToken, String refreshToken) {
        AuthResponse r = new AuthResponse();
        r.success = true;
        r.data = new Data(user, accessToken, refreshToken);
        r.error = null;
        return r;
    }

    public static AuthResponse fail(String code, String message, Object details) {
        AuthResponse r = new AuthResponse();
        r.success = false;
        r.data = null;
        r.error = new ErrorInfo(code, message, details);
        return r;
    }

    public boolean isSuccess() { return success; }
    public Data getData() { return data; }
    public ErrorInfo getError() { return error; }
    public String getTimestamp() { return timestamp; }

    public static class Data {
        private UserInfo user;
        private String accessToken;
        private String refreshToken;

        public Data(UserInfo user, String accessToken, String refreshToken) {
            this.user = user;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public UserInfo getUser() { return user; }
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }

    public static class UserInfo {
        private String email;
        private String firstname;
        private String lastname;
        private String role;

        public UserInfo(String email, String firstname, String lastname, String role) {
            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
            this.role = role;
        }

        public String getEmail() { return email; }
        public String getFirstname() { return firstname; }
        public String getLastname() { return lastname; }
        public String getRole() { return role; }
    }

    public static class ErrorInfo {
        private String code;
        private String message;
        private Object details;

        public ErrorInfo(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }

        public String getCode() { return code; }
        public String getMessage() { return message; }
        public Object getDetails() { return details; }
    }
}