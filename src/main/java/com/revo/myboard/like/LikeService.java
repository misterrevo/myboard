package com.revo.myboard.like;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentServiceApi;
import com.revo.myboard.exception.CommentNotExistsException;
import com.revo.myboard.exception.HasLikeBeforeException;
import com.revo.myboard.exception.PostNotExistsException;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostServiceApi;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Lazy))
class LikeService implements LikeServiceApi{

    private final LikeRepository repository;
    private final UserServiceApi userService;
    private final PostServiceApi postService;
    private final CommentServiceApi commentService;

    @Override
    public LikeDTO giveForPostById(String token, long post_id) {
        var user = getUser(token);
        var post = getPost(post_id);
        if (hasLikeInPost(user, post)) {
            throw new HasLikeBeforeException(post_id);
        }
        var like = repository.save(buildForPost(user, post));
        return mapFromLike(like);
    }

    private boolean hasLikeInPost(User user, Post post) {
        return post.getMyLikes().stream()
                .anyMatch(like -> isOwnerOfLike(like, user));
    }

    private boolean isOwnerOfLike(Like like, User user) {
        return Objects.equals(like.getWho(), user);
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

    @Override
    public LikeDTO giveForCommentById(String token, long comment_id) {
        var user = getUser(token);
        var comment = getComment(comment_id);
        if (hasLikeInComment(comment, user)) {
            throw new HasLikeBeforeException(comment_id);
        }
        var like = repository.save(buildForComment(user, comment));
        return mapFromLike(like);
    }

    private boolean hasLikeInComment(Comment comment, User user) {
        return comment.getMyLikes().stream()
                .anyMatch(like -> isOwnerOfLike(like, user));
    }

    private Comment getComment(long id){
        return commentService.getCommentById(id);
    }

    private Like buildForComment(User user, Comment comment){
        return Like.builder()
                .who(user)
                .comment(comment)
                .build();
    }

    @Override
    public LikeDTO removeFromPostById(String token, long id) {
        if (!hasLikeInPost(getUser(token), getPost(id))) {
            throw new PostNotExistsException(id);
        }
        var like = getLikeFromPost(token, id);
        repository.delete(like);
        return mapFromLike(like);
    }

    private Like getLikeFromPost(String token, long id){
        return getPost(id).getMyLikes().stream()
                .filter(like -> isOwnerOfLike(like, getUser(token)))
                .findFirst()
                .orElseThrow(() -> new PostNotExistsException(id));
    }

    @Override
    public LikeDTO removeFromCommentById(String token, long id) {
        if (!hasLikeInComment(getComment(id), getUser(token))) {
            throw new CommentNotExistsException(id);
        }
        var like = getLikeFromComment(token, id);
        repository.delete(like);
        return mapFromLike(like);
    }

    private Like getLikeFromComment(String token, long id){
        return getComment(id).getMyLikes().stream()
                .filter(like -> isOwnerOfLike(like, getUser(token)))
                .findFirst()
                .orElseThrow(() -> new CommentNotExistsException(id));
    }
}
