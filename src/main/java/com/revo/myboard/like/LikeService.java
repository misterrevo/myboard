package com.revo.myboard.like;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentService;
import com.revo.myboard.exception.CommentNotExistsException;
import com.revo.myboard.exception.HasLikeBeforeException;
import com.revo.myboard.exception.PostNotExistsException;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostService;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/*
 * Created By Revo
 */

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class LikeService {

    private final LikeRepository repository;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public LikeDTO giveForPostById(String token, long post_id) {
        var user = getUser(token);
        var post = getPost(post_id);
        if (post.getMyLikes().stream().anyMatch(like -> like.getWho().equals(user))) {
            throw new HasLikeBeforeException(post_id);
        }
        var like = repository.save(buildForPost(user, post));
        return mapFromLike(like);
    }

    private Like buildForPost(User user, Post post){
        return Like.builder().who(user).post(post).build();
    }

    private User getUser(String token){
        return userService.currentLoggedUser(token);
    }

    private Post getPost(long id){
        return postService.getPostById(id);
    }

    private LikeDTO mapFromLike(Like like){
        return LikeMapper.mapLikeDTOFromLike(like);
    }

    public LikeDTO giveForCommentById(String token, long comment_id) {
        var user = getUser(token);
        var comment = getComment(comment_id);
        if (comment.getMyLikes().stream().anyMatch(like -> like.getWho().equals(user))) {
            throw new HasLikeBeforeException(comment_id);
        }
        var like = repository.save(buildForComment(user, comment));
        return mapFromLike(like);
    }

    private Comment getComment(long id){
        return commentService.getCommentById(id);
    }

    private Like buildForComment(User user, Comment comment){
        return Like.builder().who(user).comment(comment).build();
    }

    public void removeFromPostById(String token, long id) {
        if (getPost(id).getMyLikes().stream().noneMatch(like -> Objects.equals(like.getWho(), getUser(token)))) {
            throw new PostNotExistsException(id);
        }
        repository.delete(getLikeFromPost(token, id));
    }

    private Like getLikeFromPost(String token, long id){
        return getPost(id).getMyLikes().stream().filter(like -> Objects.equals(like.getWho(), getUser(token))).findFirst().get();
    }

    public void removeFromCommentById(String token, long id) {
        if (getComment(id).getMyLikes().stream().noneMatch(like -> Objects.equals(like.getWho(), getUser(token)))) {
            throw new CommentNotExistsException(id);
        }
        repository.delete(getLikeFromComment(token, id));
    }

    private Like getLikeFromComment(String token, long id){
        return getComment(id).getMyLikes().stream().filter(like -> Objects.equals(like.getWho(), getUser(token))).findFirst().get();
    }

}
