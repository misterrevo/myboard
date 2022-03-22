package com.revo.myboard.group.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
public class AuthortiyDTO {

    @Size(min = 4, max = 9)
    private String newAuthority;
}
