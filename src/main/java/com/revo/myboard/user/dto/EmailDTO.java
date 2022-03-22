package com.revo.myboard.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Data
public class EmailDTO {

    @Email
    private String email;
}
