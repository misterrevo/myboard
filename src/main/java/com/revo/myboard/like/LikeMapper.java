package com.revo.myboard.like;

import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.like.dto.ProfileLikeDTO;

public class LikeMapper {

    public static ProfileLikeDTO mapProfileLikeDTOFromLike(Like like) {
        if (like.getComment() != null) {
            return buildForCommentAsProfile(like);
        } else {
            return buildForPostAsProfile(like);
        }
    }

    private static ProfileLikeDTO buildForCommentAsProfile(Like like){
        var comment = like.getComment();
        var who = like.getWho();
        return ProfileLikeDTO.builder()
                .comment(comment.getId())
                .who(who.getLogin())
                .build();
    }

    private static ProfileLikeDTO buildForPostAsProfile(Like like){
        var post = like.getPost();
        var who = like.getWho();
        return ProfileLikeDTO.builder()
                .post(post.getId())
                .who(who.getLogin())
                .postTitle(post.getTitle())
                .lastActivity(post.getLastActiveDate())
                .build();
    }

    public static LikeDTO mapLikeDTOFromLike(Like like) {
        if (like.getComment() != null) {
            return buildForComment(like);
        } else {
            return buildForPost(like);
        }
    }

    private static LikeDTO buildForComment(Like like){
        var comment = like.getComment();
        var who = like.getWho();
        return LikeDTO.builder()
                .comment(comment.getId())
                .who(who.getLogin())
                .build();
    }

    private static LikeDTO buildForPost(Like like){
        var post = like.getPost();
        var who = like.getWho();
        return LikeDTO.builder()
                .post(post.getId())
                .who(who.getLogin())
                .build();
    }
}
