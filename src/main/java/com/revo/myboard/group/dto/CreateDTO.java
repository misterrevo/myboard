package com.revo.myboard.group.dto;

import lombok.*;

import javax.validation.constraints.Size;



@Data
public class CreateDTO {

    @Size(min = 4, max = 20)
    private String name;
    @Size(min = 4, max = 9)
    private String authority;
}
