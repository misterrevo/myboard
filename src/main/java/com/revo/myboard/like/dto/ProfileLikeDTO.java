package com.revo.myboard.like.dto;

import lombok.*;

import java.time.LocalDateTime;



@Data
@Builder
public class ProfileLikeDTO {

    private long post;
    private long comment;
    private String who;
    private String postTitle;
    private LocalDateTime lastActivity;
}
