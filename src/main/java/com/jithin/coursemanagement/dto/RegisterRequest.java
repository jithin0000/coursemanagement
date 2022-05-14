package com.jithin.coursemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "email is requried field")
    private String email;

    @NotBlank(message = "password is requried field")
    private String password;

    private String roleName="STUDENT";

}
