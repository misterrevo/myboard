package com.revo.myboard.report.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
public class ContentDTO {

    @NotEmpty
    private String content;
}
