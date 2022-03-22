package com.revo.myboard.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDTO{

    @NotEmpty
    private String description;
    @Min(1)
    @Max(99)
    private int age;
    @NotEmpty
    private String city;
    @NotEmpty
    private String page;
    private String gender;
}
