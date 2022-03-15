package com.revo.myboard.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PasswordDTO {

    @Size(min = 4, max = 20)
    private String password;

}
