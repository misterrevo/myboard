package com.revo.myboard.category.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 * Created By Revo
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateDTO {

    @Size(min = 2, max = 20)
    private String name;
    @NotNull
    private long section;

}
