package com.revo.myboard.like.dto;

import com.revo.myboard.like.Like;
import lombok.*;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class LikeDTO {

    private long post;
    private long comment;
    private String who;

}
