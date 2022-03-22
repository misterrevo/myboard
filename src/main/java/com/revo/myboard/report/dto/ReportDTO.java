package com.revo.myboard.report.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
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
