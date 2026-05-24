package edu.cit.poliquit.aquahaven.admin.dto;

import edu.cit.poliquit.aquahaven.user.entity.User;
import java.time.LocalDateTime;

public class AdminUserResponse {

    private Long          id;
    private String        firstname;
    private String        lastname;
    private String        email;
    private String        phone;
    private String        role;
    private LocalDateTime createdAt;

    private AdminUserResponse() {}

    private static String formatRole(String role) {
        if (role == null) return "CUSTOMER";
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }

    public static AdminUserResponse from(User user) {
        AdminUserResponse r = new AdminUserResponse();
        r.id        = user.getId();
        r.firstname = user.getFirstname();
        r.lastname  = user.getLastname();
        r.email     = user.getEmail();
        r.phone     = user.getPhone();
        r.role      = formatRole(user.getRole());
        r.createdAt = user.getCreatedAt();
        return r;
    }

    public Long          getId()        { return id; }
    public String        getFirstname() { return firstname; }
    public String        getLastname()  { return lastname; }
    public String        getEmail()     { return email; }
    public String        getPhone()     { return phone; }
    public String        getRole()      { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
