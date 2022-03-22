package com.revo.myboard.security.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {

    @Size(min = 4, max = 20)
    private String login;
    @Size(min = 4, max = 20)
    private String password;
    @Email
    private String email;
}
