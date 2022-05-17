package com.jithin.coursemanagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StudentRegisterRequest {

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;

    @NotBlank(message = "Username should not be blank")
    private String username;

    @NotBlank(message = "PhoneNumber should not be blank")
    private String phoneNumber;

    private String profilePicUrl="";
    private String roleName="STUDENT";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
