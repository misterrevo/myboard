package com.revo.myboard.user;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentMapper;
import com.revo.myboard.comment.dto.ShortCommentDTO;
import com.revo.myboard.like.Like;
import com.revo.myboard.like.LikeMapper;
import com.revo.myboard.like.dto.ProfileLikeDTO;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostMapper;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.report.Report;
import com.revo.myboard.report.ReportMapper;
import com.revo.myboard.report.dto.ReportDTO;
import com.revo.myboard.user.dto.DataDTO;
import com.revo.myboard.user.dto.ProfileDTO;
import com.revo.myboard.user.dto.SearchDTO;
import com.revo.myboard.user.dto.UserDTO;

import java.util.List;

public final class UserMapper {

    public static DataDTO mapDataDTOFromData(Data data) {
        return buildDataDTO(data);
    }

    private static DataDTO buildDataDTO(Data data) {
        var gender = data.getGender();
        return DataDTO.builder()
                .age(data.getAge())
                .city(data.getCity())
                .description(data.getDescription())
                .page(data.getPage())
                .gender(gender.toString())
                .build();
    }

    public static ProfileDTO mapProfileDTOFromUser(User user) {
        return buildProfileDTO(user);
    }

    private static ProfileDTO buildProfileDTO(User user){
        var group = user.getGroup();
        var authority = group.getAuthority();
        return ProfileDTO.builder()
                .data(mapDataDTOFromData(user.getData()))
                .authority(authority.toString())
                .comments(mapFromComments(user.getComments()))
                .email(user.getEmail())
                .group(group.getName())
                .liked(mapFromLikes(user.getLiked()))
                .login(user.getLogin())
                .posts(mapFromPosts(user.getPosts()))
                .reports(mapFromReports(user.getReports()))
                .build();
    }

    private static List<ReportDTO> mapFromReports(List<Report> reports) {
        return reports.stream()
                .map(ReportMapper::mapFromReport)
                .toList();
    }

    private static List<ShortPostDTO> mapFromPosts(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::mapShortPostDTOFromPost)
                .toList();
    }

    private static List<ProfileLikeDTO> mapFromLikes(List<Like> liked) {
        return liked.stream()
                .map(LikeMapper::mapProfileLikeDTOFromLike)
                .toList();
    }

    private static List<ShortCommentDTO> mapFromComments(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::mapShortCommentDTOFromComment)
                .toList();
    }

    public static SearchDTO mapSearchDTOFromUser(User user) {
        return buildSearchDTO(user);
    }

    private static SearchDTO buildSearchDTO(User user){
        return SearchDTO.builder()
                .login(user.getLogin())
                .lastActivity(user.getLastActivityDate())
                .build();
    }

    public static UserDTO mapUserDTOFromUser(User user) {
        return buildUserDTO(user);
    }

    private static UserDTO buildUserDTO(User user){
        var group = user.getGroup();
        return UserDTO.builder()
                .data(mapDataDTOFromData(user.getData()))
                .email(user.getEmail())
                .group(group.getName())
                .login(user.getLogin())
                .posts(mapFromPosts(user.getPosts()))
                .blocked(user.isBlocked())
                .active(user.isActive())
                .build();
    }
}
