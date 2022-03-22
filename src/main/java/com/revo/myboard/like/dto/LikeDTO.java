package com.revo.myboard.like.dto;

import lombok.*;



@Data
@Builder
public class LikeDTO {

    private long post;
    private long comment;
    private String who;
}
