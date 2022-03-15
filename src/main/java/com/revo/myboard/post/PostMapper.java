package com.revo.myboard.post;

import com.revo.myboard.comment.CommentMapper;
import com.revo.myboard.like.LikeMapper;
import com.revo.myboard.post.dto.PostDTO;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.report.ReportMapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 * Created By Revo
 */

public final class PostMapper {

    public static PostDTO mapPostDTOFromPost(Post post) {
        if(post.getComments() != null && post.getMyLikes() != null && post.getReports() != null){
            return buildForPost(post);
        }
        return buildForNewPost(post);
    }

    private static PostDTO buildForNewPost(Post post){
        return PostDTO.builder()
                .id(post.getId())
                .category(post.getCategory().getId())
                .comments(new ArrayList<>())
                .content(post.getContent()).date(post.getDate()).lastEditDate(post.getLastEditDate())
                .likes(new ArrayList<>())
                .reports(new ArrayList<>())
                .title(post.getTitle()).user(post.getAuthor().getLogin())
                .build();
    }

    private static PostDTO buildForPost(Post post){
        return PostDTO.builder()
                .id(post.getId())
                .category(post.getCategory().getId())
                .comments(post.getComments().stream().map(CommentMapper::mapCommentDTOFromComment).collect(Collectors.toList()))
                .content(post.getContent()).date(post.getDate()).lastEditDate(post.getLastEditDate())
                .likes(post.getMyLikes().stream().map(LikeMapper::mapLikeDTOFromLike).collect(Collectors.toList()))
                .reports(post.getReports().stream().map(ReportMapper::mapFromReport).collect(Collectors.toList()))
                .title(post.getTitle())
                .user(post.getAuthor().getLogin())
                .build();
    }

    public static ShortPostDTO mapShortPostDTOFromPost(Post post) {
        return buildShortPostDTO(post);
    }

    private static ShortPostDTO buildShortPostDTO(Post post) {
        return ShortPostDTO.builder()
                .id(post.getId())
                .lastActivity(post.getLastActiveDate())
                .title(post.getTitle())
                .author(post.getAuthor().getLogin())
                .answers(post.getComments().size())
                .build();
    }

}
