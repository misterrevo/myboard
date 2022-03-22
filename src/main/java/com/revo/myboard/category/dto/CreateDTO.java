package com.revo.myboard.category.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateDTO {

    @Size(min = 2, max = 20)
    private String name;
    @NotNull
    private long section;
}
