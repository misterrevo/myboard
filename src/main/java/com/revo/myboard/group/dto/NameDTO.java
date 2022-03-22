package com.revo.myboard.group.dto;

import lombok.*;

import javax.validation.constraints.Size;



@Data
public class NameDTO {

    @Size(min = 4, max = 20)
    private String newName;
}
