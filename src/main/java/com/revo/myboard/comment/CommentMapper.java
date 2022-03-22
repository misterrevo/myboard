package com.revo.myboard.comment;

import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.comment.dto.ShortCommentDTO;
import com.revo.myboard.like.Like;
import com.revo.myboard.like.LikeMapper;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.report.Report;
import com.revo.myboard.report.ReportMapper;
import com.revo.myboard.report.dto.ReportDTO;

import java.util.List;

public final class CommentMapper {

    public static CommentDTO mapCommentDTOFromComment(Comment comment) {
       return buildCommentDTO(comment);
    }

    private static CommentDTO buildCommentDTO(Comment comment){
        var author = comment.getAuthor();
        var post = comment.getPost();
        return CommentDTO.builder()
                .content(comment.getContent())
                .date(comment.getDate())
                .id(comment.getId())
                .lastEditDate(comment.getLastEditDate())
                .likes(mapFromLikes(comment.getMyLikes()))
                .reports(mapFromReports(comment.getReports()))
                .user(author.getLogin())
                .post(post.getId())
                .build();
    }

    private static List<ReportDTO> mapFromReports(List<Report> reports) {
        return reports.stream()
                .map(ReportMapper::mapFromReport)
                .toList();
    }

    private static List<LikeDTO> mapFromLikes(List<Like> myLikes) {
        return myLikes.stream()
                .map(LikeMapper::mapLikeDTOFromLike)
                .toList();
    }

    public static ShortCommentDTO mapShortCommentDTOFromComment(Comment comment) {
        return buildShortCommentDTO(comment);
    }

    private static ShortCommentDTO buildShortCommentDTO(Comment comment) {
        var author = comment.getAuthor();
        var post = comment.getPost();
        return ShortCommentDTO.builder()
                .id(comment.getId())
                .post(post.getId())
                .user(author.getLogin())
                .build();
    }
}
