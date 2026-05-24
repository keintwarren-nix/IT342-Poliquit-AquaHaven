package edu.cit.poliquit.aquahaven.user.dto.request;

public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private String phone;

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
