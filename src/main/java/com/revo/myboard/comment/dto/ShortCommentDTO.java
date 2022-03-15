package com.revo.myboard.comment.dto;

import com.revo.myboard.comment.Comment;
import lombok.*;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class ShortCommentDTO {

    private long id;
    private String user;
    private long post;

}
