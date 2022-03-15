package com.revo.myboard.user.dto;

import com.revo.myboard.comment.dto.ShortCommentDTO;
import com.revo.myboard.like.dto.ProfileLikeDTO;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.report.dto.ReportDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class ProfileDTO {

    private String login;
    private String authority;
    private String email;
    private List<ShortPostDTO> posts;
    private List<ShortCommentDTO> comments;
    private List<ReportDTO> reports;
    private List<ProfileLikeDTO> liked;
    private String group;
    private DataDTO data;

}
