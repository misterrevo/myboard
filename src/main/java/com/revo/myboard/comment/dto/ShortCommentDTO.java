package com.revo.myboard.comment.dto;

import lombok.*;

@Data
@Builder
public class ShortCommentDTO {

    private long id;
    private String user;
    private long post;
}
