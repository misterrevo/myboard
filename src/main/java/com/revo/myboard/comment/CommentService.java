package com.revo.myboard.comment;

import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.exception.CommentNotExistsException;
import com.revo.myboard.exception.NoPermissionException;
import com.revo.myboard.group.Authority;
import com.revo.myboard.like.LikeService;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostService;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/*
 * Created By Revo
 */

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;

    CommentDTO createComment(String token, long post_id, String content) {
        getPost(post_id).setLastActiveDate(LocalDateTime.now());
        getUser(token).setLastActivityDate(LocalDateTime.now());
        var comment = repository.save(buildComment(token, post_id, content));
        return mapFromComment(comment);
    }

    private Post getPost(long id){
        return postService.getPostById(id);
    }

    private User getUser(String token){
        return userService.currentLoggedUser(token);
    }

    private Comment buildComment(String token, long post_id, String content){
        return Comment.builder().author(userService.currentLoggedUser(token)).content(content)
                .date(LocalDateTime.now()).post(postService.getPostById(post_id))
                .lastEditDate(LocalDateTime.now()).build();
    }

    public Comment getCommentById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentNotExistsException(id));
    }

    CommentDTO getCommentDTOById(long id) {
        return CommentMapper.mapCommentDTOFromComment(getCommentById(id));
    }

    CommentDTO editCommentById(String token, long id, String content) {
        var user = getUser(token);
        if (Objects.equals(user.getGroup().getAuthority(), Authority.USER) && !user.getComments().contains(getCommentById(id))) {
            throw new NoPermissionException(user.getLogin());
        }
        var comment = getCommentById(id);
        comment.setContent(content);
        comment.setLastEditDate(LocalDateTime.now());
        return mapFromComment(comment);
    }

    private CommentDTO mapFromComment(Comment comment){
        return CommentMapper.mapCommentDTOFromComment(comment);
    }

    void deleteCommentById(String token, long id) {
        var user = userService.currentLoggedUser(token);
        if (Objects.equals(user.getGroup().getAuthority(), Authority.USER)) {
            if (user.getComments().stream().noneMatch(target -> target.getId() == id)) {
                throw new NoPermissionException(user.getLogin());
            }
        }
        repository.delete(getCommentById(id));
    }

    LikeDTO giveLikeForCommentById(String token, long id) {
        return likeService.giveForCommentById(token, id);
    }

    void removeLikeFromCommentById(String token, long id) {
        likeService.removeFromCommentById(token, id);
    }

}
