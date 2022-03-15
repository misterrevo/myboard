package com.revo.myboard.like.dto;

import com.revo.myboard.like.Like;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class ProfileLikeDTO {

    private long post;
    private long comment;
    private String who;
    private String postTitle;
    private LocalDateTime lastActivity;

}
