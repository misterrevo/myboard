package com.revo.myboard.comment.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateDTO {

    @NotNull
    private long post;
    @NotEmpty
    private String content;
}
