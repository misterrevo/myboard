package com.revo.myboard.security.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CredentialsDTO {

    @Size(min = 4, max = 20)
    private String login;
    @Size(min = 4, max = 20)
    private String password;
}
