package com.revo.myboard.group.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

/*
 * Created By Revo
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateDTO {

    @Size(min = 4, max = 20)
    private String name;
    @Size(min = 4, max = 9)
    private String authority;

}
