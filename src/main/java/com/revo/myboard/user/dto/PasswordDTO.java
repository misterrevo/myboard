package com.revo.myboard.user.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
public class PasswordDTO {

    @Size(min = 4, max = 20)
    private String password;
}
