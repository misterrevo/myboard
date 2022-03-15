package com.revo.myboard.security.dto;

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
public class CredentialsDTO {

    @Size(min = 4, max = 20)
    private String login;
    @Size(min = 4, max = 20)
    private String password;

}
