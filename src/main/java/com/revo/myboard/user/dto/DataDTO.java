package com.revo.myboard.user.dto;

import com.revo.myboard.user.Data;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DataDTO {

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
