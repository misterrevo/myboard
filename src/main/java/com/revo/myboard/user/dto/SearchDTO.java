package com.revo.myboard.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchDTO {

    private String login;
    private LocalDateTime lastActivity;
}
