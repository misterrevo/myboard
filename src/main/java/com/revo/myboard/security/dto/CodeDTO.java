package com.revo.myboard.security.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
public class CodeDTO {

    @Size(min = 36, max = 36)
    private String code;
}
