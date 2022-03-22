package com.revo.myboard.comment.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
public class ContentDTO {

    @NotEmpty
    private String newContent;
}
