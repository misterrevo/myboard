package com.revo.myboard.comment;

/*
 * Created By Revo
 */

import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.comment.dto.ShortCommentDTO;
import com.revo.myboard.like.LikeMapper;
import com.revo.myboard.report.ReportMapper;

import java.util.ArrayList;

public final class CommentMapper {

    public static CommentDTO mapCommentDTOFromComment(Comment comment) {
       if(comment.getMyLikes() != null && comment.getReports() != null){
           return buildForComment(comment);
       }
       return buildForNewComment(comment);
    }

    private static CommentDTO buildForNewComment(Comment comment){
        return CommentDTO.builder().content(comment.getContent()).date(comment.getDate()).id(comment.getId())
                .lastEditDate(comment.getLastEditDate())
                .likes(new ArrayList<>())
                .reports(new ArrayList<>())
                .user(comment.getAuthor().getLogin()).post(comment.getPost().getId()).build();
    }

    private static CommentDTO buildForComment(Comment comment){
        return CommentDTO.builder().content(comment.getContent()).date(comment.getDate()).id(comment.getId())
                .lastEditDate(comment.getLastEditDate())
                .likes(comment.getMyLikes().stream().map(LikeMapper::mapLikeDTOFromLike).toList())
                .reports(comment.getReports().stream().map(ReportMapper::mapFromReport).toList())
                .user(comment.getAuthor().getLogin()).post(comment.getPost().getId()).build();
    }

    public static ShortCommentDTO mapShortCommentDTOFromComment(Comment comment) {
        return buildShortCommentDTO(comment);
    }

    private static ShortCommentDTO buildShortCommentDTO(Comment comment) {
        return ShortCommentDTO.builder()
                .id(comment.getId())
                .post(comment.getPost().getId())
                .user(comment.getAuthor().getLogin())
                .build();
    }

}
