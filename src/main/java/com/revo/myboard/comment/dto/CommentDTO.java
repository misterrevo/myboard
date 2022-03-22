package com.revo.myboard.comment.dto;

import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.report.dto.ReportDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class CommentDTO {

    private long id;
    private String user;
    private String content;
    private LocalDateTime date;
    private LocalDateTime lastEditDate;
    private List<ReportDTO> reports;
    private long post;
    private List<LikeDTO> likes;
}
