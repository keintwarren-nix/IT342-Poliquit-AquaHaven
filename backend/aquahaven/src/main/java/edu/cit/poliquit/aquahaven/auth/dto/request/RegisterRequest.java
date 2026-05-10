package edu.cit.poliquit.aquahaven.auth.dto.request;

public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;

    public String getFirstname()                 { return firstname; }
    public void   setFirstname(String v)         { this.firstname = v; }
    public String getLastname()                  { return lastname; }
    public void   setLastname(String v)          { this.lastname = v; }
    public String getEmail()                     { return email; }
    public void   setEmail(String v)             { this.email = v; }
    public String getPhone()                     { return phone; }
    public void   setPhone(String v)             { this.phone = v; }
    public String getPassword()                  { return password; }
    public void   setPassword(String v)          { this.password = v; }
}