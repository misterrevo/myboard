package com.revo.myboard.user;

import com.revo.myboard.comment.CommentMapper;
import com.revo.myboard.like.LikeMapper;
import com.revo.myboard.post.PostMapper;
import com.revo.myboard.report.ReportMapper;
import com.revo.myboard.user.dto.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 * Created By Revo
 */

public final class UserMapper {

    public static DataDTO mapDataDTOFromData(Data data) {
        return buildDataDTO(data);
    }

    private static DataDTO buildDataDTO(Data data) {
        return DataDTO.builder()
                .age(data.getAge())
                .city(data.getCity())
                .description(data.getDescription())
                .page(data.getPage())
                .gender(data.getGender().toString())
                .build();
    }

    public static ProfileDTO mapProfileDTOFromUser(User user) {
        return buildProfileDTO(user);
    }

    private static ProfileDTO buildProfileDTO(User user){
        return ProfileDTO.builder()
                .data(mapDataDTOFromData(user.getData()))
                .authority(user.getGroup().getAuthority().toString())
                .comments(user.getComments().stream().map(CommentMapper::mapShortCommentDTOFromComment).collect(Collectors.toList()))
                .email(user.getEmail()).group(user.getGroup().getName())
                .liked(user.getLiked().stream().map(LikeMapper::mapProfileLikeDTOFromLike).collect(Collectors.toList()))
                .login(user.getLogin())
                .posts(user.getPosts().stream().map(PostMapper::mapShortPostDTOFromPost).collect(Collectors.toList()))
                .reports(user.getReports().stream().map(ReportMapper::mapFromReport).toList())
                .build();
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
        if(user.getPosts() != null){
            return buildForUser(user);
        }
        return buildForNewUser(user);
    }

    private static UserDTO buildForNewUser(User user){
        return UserDTO.builder()
                .data(mapDataDTOFromData(user.getData()))
                .email(user.getEmail())
                .group(user.getGroup().getName())
                .login(user.getLogin())
                .posts(new ArrayList<>())
                .blocked(user.isBlocked())
                .active(user.isActive())
                .build();
    }

    private static UserDTO buildForUser(User user){
        return UserDTO.builder()
                .data(mapDataDTOFromData(user.getData()))
                .email(user.getEmail())
                .group(user.getGroup().getName())
                .login(user.getLogin())
                .posts(user.getPosts().stream().map(PostMapper::mapShortPostDTOFromPost).collect(Collectors.toList()))
                .blocked(user.isBlocked())
                .active(user.isActive())
                .build();
    }

}
