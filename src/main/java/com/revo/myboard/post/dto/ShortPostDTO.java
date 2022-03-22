package com.revo.myboard.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class ShortPostDTO {

    private long id;
    private String title;
    private LocalDateTime lastActivity;
    private String author;
    private int answers;
}
