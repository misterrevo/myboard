package com.revo.myboard.user.dto;

import lombok.*;

import java.time.LocalDateTime;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class SearchDTO {

    private String login;
    private LocalDateTime lastActivity;

}
