package com.revo.myboard.like;

import com.revo.myboard.like.dto.LikeDTO;

public interface LikeServiceApi {
    LikeDTO giveForPostById(String token, long post_id);

    LikeDTO giveForCommentById(String token, long comment_id);

    LikeDTO removeFromPostById(String token, long id);

    LikeDTO removeFromCommentById(String token, long id);
}
