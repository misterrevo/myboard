package com.revo.myboard.comment;

import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.comment.dto.CreateDTO;
import com.revo.myboard.exception.CommentNotExistsException;
import com.revo.myboard.exception.NoPermissionException;
import com.revo.myboard.group.Authority;
import com.revo.myboard.like.LikeServiceApi;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostServiceApi;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class CommentService implements CommentServiceApi{

    private final CommentRepository repository;
    private final UserServiceApi userService;
    private final PostServiceApi postService;
    private final LikeServiceApi likeService;

    CommentDTO createComment(String token, CreateDTO createDTO) {
        var content = createDTO.getContent();
        var post_id = createDTO.getPost();
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
        return Comment.builder()
                .author(getUser(token))
                .content(content)
                .date(LocalDateTime.now())
                .post(getPost(post_id))
                .lastEditDate(LocalDateTime.now())
                .build();
    }

    @Override
    public Comment getCommentById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentNotExistsException(id));
    }

    CommentDTO getCommentDTOById(long id) {
        return mapFromComment(getCommentById(id));
    }

    CommentDTO editCommentById(String token, long id, String content) {
        var user = getUser(token);
        if (hasUserRole(user) && isNotOwnerOfComment(user, id)) {
            throw new NoPermissionException(user.getLogin());
        }
        var comment = getCommentById(id);
        comment.setContent(content);
        comment.setLastEditDate(LocalDateTime.now());
        return mapFromComment(comment);
    }

    private boolean isNotOwnerOfComment(User user, long id) {
        return !user.getComments().contains(getCommentById(id));
    }

    private boolean hasUserRole(User user) {
        return Objects.equals(user.getGroup().getAuthority(), Authority.USER);
    }

    private CommentDTO mapFromComment(Comment comment){
        return CommentMapper.mapCommentDTOFromComment(comment);
    }

    void deleteCommentById(String token, long id) {
        var user = userService.currentLoggedUser(token);
        if (hasUserRole(user) && isNotOwnerOfComment(user, id)) {
            throw new NoPermissionException(user.getLogin());
        }
        repository.delete(getCommentById(id));
    }

    LikeDTO giveLikeForCommentById(String token, long id) {
        return likeService.giveForCommentById(token, id);
    }

    LikeDTO removeLikeFromCommentById(String token, long id) {
        return likeService.removeFromCommentById(token, id);
    }
}
