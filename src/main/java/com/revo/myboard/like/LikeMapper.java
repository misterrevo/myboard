package com.revo.myboard.like;

/*
 * Created By Revo
 */

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
        return ProfileLikeDTO.builder()
                .comment(like.getComment().getId())
                .who(like.getWho().getLogin())
                .build();
    }

    private static ProfileLikeDTO buildForPostAsProfile(Like like){
        return ProfileLikeDTO.builder()
                .post(like.getPost().getId())
                .who(like.getWho().getLogin())
                .postTitle(like.getPost().getTitle())
                .lastActivity(like.getPost().getLastActiveDate())
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
        return LikeDTO.builder().comment(like.getComment().getId())
                .who(like.getWho().getLogin()).build();
    }

    private static LikeDTO buildForPost(Like like){
        return LikeDTO.builder().post(like.getPost().getId())
                .who(like.getWho().getLogin()).build();
    }

}
