package com.revo.myboard.post;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentMapper;
import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.like.Like;
import com.revo.myboard.like.LikeMapper;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.dto.PostDTO;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.report.Report;
import com.revo.myboard.report.ReportMapper;
import com.revo.myboard.report.dto.ReportDTO;

import java.util.List;

public final class PostMapper {

    public static PostDTO mapPostDTOFromPost(Post post) {
        return buildPostDTO(post);
    }

    private static PostDTO buildPostDTO(Post post){
        var category = post.getCategory();
        var author = post.getAuthor();
        return PostDTO.builder()
                .id(post.getId())
                .category(category.getId())
                .comments(mapFromComments(post.getComments()))
                .content(post.getContent())
                .date(post.getDate())
                .lastEditDate(post.getLastEditDate())
                .likes(mapFromLikes(post.getMyLikes()))
                .reports(mapFromReports(post.getReports()))
                .title(post.getTitle())
                .user(author.getLogin())
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

    private static List<CommentDTO> mapFromComments(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::mapCommentDTOFromComment)
                .toList();
    }

    public static ShortPostDTO mapShortPostDTOFromPost(Post post) {
        return buildShortPostDTO(post);
    }

    private static ShortPostDTO buildShortPostDTO(Post post) {
        var author = post.getAuthor();
        var comments = post.getComments();
        return ShortPostDTO.builder()
                .id(post.getId())
                .lastActivity(post.getLastActiveDate())
                .title(post.getTitle())
                .author(author.getLogin())
                .answers(comments.size())
                .build();
    }
}
