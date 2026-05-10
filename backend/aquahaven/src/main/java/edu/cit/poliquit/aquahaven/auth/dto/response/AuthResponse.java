package edu.cit.poliquit.aquahaven.auth.dto.response;

public class AuthResponse {

    private boolean  success;
    private String   errorCode;
    private String   message;
    private UserInfo user;
    private String   accessToken;
    private String   refreshToken;

    private AuthResponse() {}

    public static AuthResponse ok(UserInfo user, String accessToken, String refreshToken) {
        AuthResponse r = new AuthResponse();
        r.success      = true;
        r.user         = user;
        r.accessToken  = accessToken;
        r.refreshToken = refreshToken;
        return r;
    }

    public static AuthResponse fail(String errorCode, String message, UserInfo user) {
        AuthResponse r = new AuthResponse();
        r.success   = false;
        r.errorCode = errorCode;
        r.message   = message;
        r.user      = user;
        return r;
    }

    public boolean  isSuccess()       { return success; }
    public String   getErrorCode()    { return errorCode; }
    public String   getMessage()      { return message; }
    public UserInfo getUser()         { return user; }
    public String   getAccessToken()  { return accessToken; }
    public String   getRefreshToken() { return refreshToken; }

    public static class UserInfo {
        private String email;
        private String firstname;
        private String lastname;
        private String role;

        public UserInfo(String email, String firstname, String lastname, String role) {
            this.email     = email;
            this.firstname = firstname;
            this.lastname  = lastname;
            this.role      = role;
        }

        public String getEmail()     { return email; }
        public String getFirstname() { return firstname; }
        public String getLastname()  { return lastname; }
        public String getRole()      { return role; }
    }
}