package com.revo.myboard.report.dto;

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
public class ReportDTO {

    private long id;
    private String reporter;
    private long post;
    private String postTitle;
    private long comment;
    private LocalDateTime date;
    private boolean checked;
    private String content;

}
