package com.revo.myboard.post.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateDTO {

    @NotNull
    private long category;
    @Size(min = 4, max = 30)
    private String title;
    @NotEmpty
    private String content;
}
