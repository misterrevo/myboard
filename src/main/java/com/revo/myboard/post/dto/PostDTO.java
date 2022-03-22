package com.revo.myboard.post.dto;

import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.report.dto.ReportDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDTO {

    private long id;
    private String user;
    private String title;
    private String content;
    private List<CommentDTO> comments;
    private LocalDateTime date;
    private LocalDateTime lastEditDate;
    private long category;
    private List<ReportDTO> reports;
    private List<LikeDTO> likes;
}
