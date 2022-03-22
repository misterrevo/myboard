package com.revo.myboard.category.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
public class NameDTO {

    @Size(min = 2, max = 20)
    private String newName;
}
